package com.binarymj.DenialTracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ArrayList<Subject> MySubjects;//save this
    private Subject CurrentSubject = null;
    private ListAdaptor ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Load();
        UpdateList();
    }

    private void Save() {
        SharedPreferences sharedPreferences = getSharedPreferences("Subjects", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MySubjects);
        editor.putString("TheSubjects", json);
        editor.apply();
    }

    private void Load() {
            SharedPreferences sharedPreferences = getSharedPreferences("Subjects", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("TheSubjects", null);
            Type type = new TypeToken<ArrayList<Subject>>() {
            }.getType();
            MySubjects = gson.fromJson(json, type);
        if (MySubjects == null) {//first time opening the app
            MySubjects = new ArrayList<>();
        }
    }

    public void UpdateList() {
        ad = new ListAdaptor(MySubjects);
        ad.setC(this);
        ListView theList = findViewById(R.id.listView);

        theList.setAdapter(ad);
        theList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoIntoSubject(MySubjects.get(position));
            }
        });
        Save();
    }

    public void SubjectAdd(View view) {
        setContentView(R.layout.add_subject);
        TextView dnStat = findViewById(R.id.dnStat);
        dnStat.setText("Note: the DN rate is set to " + Subject.DNrate + "% , can be changed in the settings.");
    }

    public void AddSubject(View view) {
        EditText TakeSubjectName = findViewById(R.id.editText);
        String Sn = TakeSubjectName.getText().toString();

        try {
            EditText TakeSubjectCredit = findViewById(R.id.editText2);
            int Credit = Integer.parseInt(TakeSubjectCredit.getText().toString());

            EditText TakeSubjectWeeks = findViewById(R.id.editText3);
            int Weeks = Integer.parseInt(TakeSubjectWeeks.getText().toString());


            MySubjects.add(new Subject(Sn, Credit, Weeks));

        } catch (NumberFormatException x) {
            new AlertDialog.Builder(this).setTitle("Error").setMessage("Please enter integer digits only for credit and weeks").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
        setContentView(R.layout.activity_main);
        UpdateList();
    }

    public void GoIntoSubject(final Subject x) {
        CurrentSubject = x;
        setContentView(R.layout.newabsentscreen);
        TextView TakeSubjectName = findViewById(R.id.subjectName);
        TakeSubjectName.setText(x.getName());
        TextView NumOfA = findViewById(R.id.NumOfA);
        NumOfA.setText(" " + x.getNumAbsens());

        ListAdaptor2 ad2 = new ListAdaptor2(x.getAllAbsens(), x.getAbsenseCause());
        ad2.setC(this);
        ListView datesList = findViewById(R.id.aList);
        datesList.setAdapter(ad2);

        datesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                CurrentSubject.DeleteAbsens(position);
                                GoIntoSubject(x);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //return;
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        Save();
    }

    // new app
    public void AddNewapp(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "Date picker");
    }

    public void Back(View view) {
        setContentView(R.layout.activity_main);
        UpdateList();
    }

    private String m_Text = "";

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cause of absence");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                CurrentSubject.newAbsens(c.getTime(), m_Text);
                GoIntoSubject(CurrentSubject);

            }
        });
        builder.setNegativeButton("No need", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CurrentSubject.newAbsens(c.getTime(), "");
                dialog.cancel();
                GoIntoSubject(CurrentSubject);
            }
        });
        builder.show();
    }

    public void Settings(View view) {
        setContentView(R.layout.settings);
        TextView dnStat = findViewById(R.id.dnStatt);
        dnStat.setText("the DN rate is set to " + Subject.DNrate + "%");
    }

    public void SaveSettings(View view) {
        TextView DN = findViewById(R.id.Dnrate);
        try {
            double Dn = Double.parseDouble(DN.getText().toString());
            if (Dn < 0 || Dn > 100) {
                new AlertDialog.Builder(this).setTitle("Error").setMessage("DN rate should be between 0 to 100").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
            } else {
                Subject.DNrate = Dn;
            }

        } catch (NumberFormatException x) {//
            new AlertDialog.Builder(this).setTitle("Error").setMessage("Please enter digits only").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            }).setIcon(android.R.drawable.ic_dialog_alert).show();
        }
        TextView dnStat = (TextView) findViewById(R.id.dnStatt);
        dnStat.setText("the DN rate is set to " + Subject.DNrate + "%");
    }

    public void deleteSubject(View view) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        MySubjects.remove(CurrentSubject);
                        setContentView(R.layout.activity_main);
                        UpdateList();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

}

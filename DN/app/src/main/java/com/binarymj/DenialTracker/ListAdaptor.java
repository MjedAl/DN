package com.binarymj.DenialTracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdaptor extends BaseAdapter {

    ArrayList<Subject> ListOfSubjects;
    Context c;

    public ListAdaptor(ArrayList<Subject> listOfSubjects) {
        this.ListOfSubjects = listOfSubjects;
    }

    public void setC(Context c) {
        this.c = c;
    }


    @Override
    public int getCount() {
//        if(ListOfSubjects==null){
//           return 0;
//        }
        return ListOfSubjects.size();
    }

    @Override
    public Object getItem(int position) {
        return ListOfSubjects.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.row_item, parent, false);

        TextView name = (TextView) view.findViewById(R.id.subjectNamee);
        TextView fN = (TextView) view.findViewById(R.id.firstNumber);
        TextView sN = (TextView) view.findViewById(R.id.SecondNumber);



       // LinearLayout layout = (LinearLayout)view.findViewById(R.id.Llayout);
     //   ViewGroup.LayoutParams params = layout.getLayoutParams();

       // params.height = (1600/ListOfSubjects.size());
       // layout.setLayoutParams(params);

        int num = ListOfSubjects.get(position).getNumAbsens();
        int aN = ListOfSubjects.get(position).getAllowedNum();

        ProgressBar My = view.findViewById(R.id.MyProgressBar);
        My.setMax(aN);
        My.setProgress(num);

//        if (num<(aN/4)) {
//            view.findViewById(R.id.LinearLayout).setBackgroundColor(Color.WHITE);
//        } else if (num>=(aN/4) && num<(aN/2)) {
//            view.findViewById(R.id.LinearLayout).setBackgroundColor(Color.GRAY);
//        } else if (num>(aN/2)) {
//            view.findViewById(R.id.LinearLayout).setBackgroundColor(Color.RED);
//        }

        name.setText(ListOfSubjects.get(position).getName());
        fN.setText("" + num);
        sN.setText(" " + aN);
        return view;
    }
}

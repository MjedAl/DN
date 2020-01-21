package com.binarymj.DenialTracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListAdaptor2 extends BaseAdapter {

    ArrayList<Date> ListOfDates;
    ArrayList<String> causee;
    Context c;

    public ListAdaptor2(ArrayList<Date> listOfSubjects,ArrayList<String> cause) {
        this.ListOfDates = listOfSubjects;
        this.causee = cause;
    }

    public void setC(Context c) {
        this.c = c;
    }


    @Override
    public int getCount() {
        return ListOfDates.size();
    }

    @Override
    public Object getItem(int position) {
        return ListOfDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(c);
        View view = inflater.inflate(R.layout.date_layout, parent, false);

        TextView date = (TextView) view.findViewById(R.id.textView);
        SimpleDateFormat my = new SimpleDateFormat("dd / MM / yyyy");
        Date temp = ListOfDates.get(position);
        date.setText(my.format(temp));
        System.out.println(my.format(temp));

        TextView cause = (TextView) view.findViewById(R.id.textView3);
        cause.setText(causee.get(position));

        return view;
    }
}

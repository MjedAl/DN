package com.binarymj.DenialTracker;
import java.util.ArrayList;
import java.util.Date;

public class Subject {
    public static double DNrate = 25;
    private String name;
    private int classes;
    private Date addDate;
    private ArrayList<Date> allAbsens = new ArrayList();
    private ArrayList<String> AbsenseCause = new ArrayList();
    private int Weeks;



    public String getName() {
        return name;
    }

    public Subject(String name, int classes,int Weeks) {
        this.name = name;
        this.classes = classes;
        this.addDate = new Date();//add date
        this.Weeks= Weeks;
    }

    public void DeleteAbsens(int Position){
        allAbsens.remove(Position);
        AbsenseCause.remove(Position);
    }
    public int getAllowedNum(){
        return (int) Math.floor(((DNrate/100.00)*Weeks*classes));
    }

    public int getNumAbsens() {
        return allAbsens.size();
    }

    public ArrayList<Date> getAllAbsens() {
        return allAbsens;
    }

    public ArrayList<String> getAbsenseCause() {
        return AbsenseCause;
    }

    public void newAbsens(Date date, String cause){
        allAbsens.add(date);
        AbsenseCause.add(cause);
    }
}

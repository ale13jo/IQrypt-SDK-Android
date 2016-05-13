package com.iqrypt_firebase.Repository.models;

/**
 * Created by Cristi on 3/23/2016.
 */ // This class contains patient's intake|output data
public class IntakeOutput
{

    public String Date;

    public String Intake;

    public String Output;

    // Constructor
    public IntakeOutput(String date, String intake, String output)
    {
        this.Date = date;
        this.Intake = intake;
        this.Output = output;
    }
    public IntakeOutput(){}
}

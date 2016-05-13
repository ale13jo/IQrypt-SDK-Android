package com.iqrypt_firebase.Repository.models;

/**
 * Created by Cristi on 3/23/2016.
 */ // This class contains patient's admission record data
 public class AdmissionRecords
{
    public String Doctor;

    public String HealthcareProvider;

    public String Address;

    public String Phone;

    public String DateAdmitted;
    public String DateDischarged;
    public static AdmissionRecords Example()
    {
        AdmissionRecords admissionRecords= new AdmissionRecords();
        admissionRecords.Doctor = "Dr. John ollock";
        admissionRecords.HealthcareProvider = "AETNA";
        admissionRecords.Address = "4122 SE 1st PL    Newcastle, WA 98056";
        admissionRecords.Phone = "123.456.7890";
        admissionRecords.DateAdmitted = "Feb. 10, 2016";
        admissionRecords.DateDischarged = "N/A";
        return admissionRecords;
    }
}

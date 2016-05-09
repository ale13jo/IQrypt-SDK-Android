package dotissi.iqrypt_couchbaselite_sample.Repository.models;

import java.util.Date;

import dotissi.iqrypt_couchbaselite_sample.Repository.models.PatientDetails;

 public class Patient
{
    // Patient object contains following properties
    public String ObjectId ;
    public String SSN ;
    public String PhotoThumbnail ;
    public String PhotoDetail ;
    public String FirstName ;
    public String LastName ;
    public String Gender ;
    public String Location ;
    public String Building ;
    public int Floor ;
    public Date VisitDate;
    public String Treatment ;
    public String Status ;
    public String Doctor ;
    public PatientDetails PatientDetailsData;
    public String Version ;
    public boolean IsVisited;



}



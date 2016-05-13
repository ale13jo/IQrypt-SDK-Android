package com.iqrypt_firebase.Repository.models;

import java.util.ArrayList;



/**
* Created by Cristi on 3/23/2016.
*/
public class PatientDetails {
   public BloodGas Blood_Gas;

   public Blood BloodData;

   public GeneralChemistry General_Chemistry;
   public Urinalysis UrinalysisData;

   public ArrayList<ProgressNotes> Progress_Notes;

   public ArrayList<IntakeOutput> Intake_Output ;

   public AdmissionRecords Admission_Records;
   public static PatientDetails Example()
   {
       PatientDetails example = new PatientDetails();
       example.Progress_Notes=new ArrayList<>();
       example.Progress_Notes.add(new ProgressNotes("Feb", "14", "Patient admitted complaining of abdominal pain.   Administered 20 ml of Trx.  Will plan to review condition in 6 hours.   Submitted request for blood work 2/15.  Dr Pollock will be..."));
       example.Progress_Notes.add(new ProgressNotes("Feb", "15", "Current treatment plan appears to be progressing well.  Submitted dietary change request.   Have requested that patient remain on modified diet for 4 days.  Patient family..."));
       example.Progress_Notes.add(new ProgressNotes("Feb", "16", "Current treatment plan appears to be progressing well.  Submitted dietary change request.   Have requested that patient remain on modified diet for 4 days.  Patient family..."));

       example.Intake_Output=new ArrayList<>();
       example.Intake_Output.add(new IntakeOutput("2/11", "750ml", "680ml"));
       example.Intake_Output.add(new IntakeOutput("2/12", "520ml", "540ml"));
       example.Intake_Output.add(new IntakeOutput("2/13", "820ml", "760ml"));
       example.Intake_Output.add(new IntakeOutput("2/14", "500ml", "520ml"));

       example.Admission_Records = AdmissionRecords.Example();
       example.Blood_Gas = BloodGas.Example();
       example.BloodData = Blood.Example();
       example.General_Chemistry = GeneralChemistry.Example();
       example.UrinalysisData = Urinalysis.Example();

       return example;

   }

}

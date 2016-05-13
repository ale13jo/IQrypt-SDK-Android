package com.iqrypt_firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;
import com.iqrypt_firebase.Repository.FirebaseRepository;
import com.iqrypt_firebase.Repository.models.Patient;
import com.iqrypt_firebase.Repository.models.PatientDetails;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);
        doFirebaseOperations();
    }
    private void doFirebaseOperations()
    {
        FirebaseRepository repo=new FirebaseRepository();
        for (int i = 0; i < 10; i++) {

            Patient patient=new Patient();
            patient.FirstName="John"+i;
            patient.LastName="Smith"+i;
            patient.SSN="423-423-423-"+i;
            patient.Building="A";
            patient.Floor=i;
            patient.Gender="m";
            patient.Status="OK";
            patient.PatientDetailsData= PatientDetails.Example();

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,2016);
            cal.set(Calendar.MONTH,i);
            cal.set(Calendar.DAY_OF_MONTH,i+1);
            Date date  = cal.getTime();
            patient.VisitDate=date;

            repo.createPatient(patient);



        }
       repo.getPatientBySSN("423-423-423-6");

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,2016);
        cal.set(Calendar.MONTH,4);
        cal.set(Calendar.DAY_OF_MONTH,4);
        Date start  = cal.getTime();

        cal.set(Calendar.YEAR,2016);
        cal.set(Calendar.MONTH,6);
        cal.set(Calendar.DAY_OF_MONTH,7);
        Date end  = cal.getTime();

        repo.getPatientsVisitedBetween(start, end);

    }
}

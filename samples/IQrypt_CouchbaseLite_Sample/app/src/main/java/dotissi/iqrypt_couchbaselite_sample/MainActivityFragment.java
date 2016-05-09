package dotissi.iqrypt_couchbaselite_sample;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dotissi.iqrypt_couchbaselite_sample.Repository.BestPracticeRepository;
import dotissi.iqrypt_couchbaselite_sample.Repository.models.Patient;
import dotissi.iqrypt_couchbaselite_sample.Repository.SimpleRepository;
import dotissi.iqrypt_couchbaselite_sample.Repository.models.PatientDetails;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        DoStuff_SimpleRepository();
        DoStuff_BestPracticeRepository();

        return rootView;
    }


    private void DoStuff_SimpleRepository() {

        SimpleRepository repo = new SimpleRepository(getActivity().getBaseContext());

        Date now = new Date();
        String nowString = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(now);

        //add 10 docs in database
        for (int i = 0; i < 10; i++) {
            Map<String, Object> docContent = new HashMap<String, Object>();
            docContent.put("email", "hello" + i + "@iqrypt.com");
            docContent.put("registered", nowString);
            docContent.put("score", 190 + i);
            docContent.put("info", "Some sensitive info" + i);

            String docId = repo.create(docContent);
            Log.d("LOG_CRT", "Doc with id:"+docId+" created");

        }
        Map<String, Object> doc = repo.getDocByEmail("hello5@iqrypt.com");

        Log.d("LOG_EML", "email:"+doc.get("email"));
        Log.d("LOG_EML", "info:"+doc.get("info"));
        Log.d("LOG_EML", "score:"+doc.get("score"));

        List<Map<String, Object>> docsByScore = repo.getAllDocsBetweenScore(193,195);

        for(Map<String, Object> d: docsByScore)
        {
            Log.d("LOG_SCR", "email:"+d.get("email"));
            Log.d("LOG_SCR", "info:"+d.get("info"));
            Log.d("LOG_SCR", "score:"+d.get("score"));
        }

    }


    private void DoStuff_BestPracticeRepository() {
        BestPracticeRepository repo=new BestPracticeRepository(getActivity().getBaseContext());
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

            Log.d("LOG_PATIENT", "Patient with id:" + patient.ObjectId + " created");

        }
        Patient p=repo.getPatientBySSN("423-423-423-6");
        Log.d("LOG_PATIENT_BY_SSN", "Patient with SSN:" + p.SSN +" loaded" );

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,2016);
        cal.set(Calendar.MONTH,4);
        cal.set(Calendar.DAY_OF_MONTH,5);
        Date start  = cal.getTime();

        cal.set(Calendar.YEAR,2016);
        cal.set(Calendar.MONTH,6);
        cal.set(Calendar.DAY_OF_MONTH,7);
        Date end  = cal.getTime();

        List<Patient> patients=repo.getPatientsVisitedBetween(start, end);
        for (Patient p1: patients)
        {
            Log.d("LOG_PATIENT_BY_VISIT", "Patient with VisitDate:" + p1.VisitDate.toString() +" loaded" );

        }

    }


}

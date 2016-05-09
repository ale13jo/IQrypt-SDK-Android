package dotissi.iqrypt_couchbaselite_sample.Repository;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.iqrypt.Cipher;
import com.iqrypt.EncryptionType;
import com.iqrypt.EncryptorFactory;
import com.iqrypt.IEncryptor;
import com.iqrypt.IQryptConfigurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dotissi.iqrypt_couchbaselite_sample.Repository.models.Patient;


public class BestPracticeRepository {
    private Database database;
    private android.content.Context ctx;
    private Manager manager;
    IEncryptor encryptorRND;
    IEncryptor encryptorDET ;
    IEncryptor encryptorOPE ;
    public BestPracticeRepository(Context context)
    {
        this.ctx=context;
        try {
            manager = new Manager(new AndroidContext(ctx), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {

            database = manager.getDatabase("patients");
            database.delete();
            database = manager.getDatabase("patients");
            // Create a view and register its map function:
            View emailView = database.getView("ssn");
            if (emailView.getMap() == null) {
                emailView.setMap(new Mapper() {
                    @Override
                    public void map(Map<String, Object> document, Emitter emitter) {

                        emitter.emit(document.get("ssn"), null);

                    }

                }, "2");
            }
            View scoreView = database.getView("visitdate");
            if (scoreView.getMap() == null) {
                scoreView.setMap(new Mapper() {
                    @Override
                    public void map(Map<String, Object> document, Emitter emitter) {

                        emitter.emit(document.get("visitdate"), null);

                    }

                }, "2");
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();

        }
        //init IQrypt
        IQryptConfigurator.setEncryptionChiper(Cipher.AES256, "my_super_secret");
        encryptorRND = EncryptorFactory.getEncryptor(EncryptionType.RND);
        encryptorDET = EncryptorFactory.getEncryptor(EncryptionType.DET);
        encryptorOPE = EncryptorFactory.getEncryptor(EncryptionType.OPE);
    }
    public String createPatient(Patient patient) {

        Document doc = database.createDocument();
        // add content to document and write the document to the database
        try {

            patient.ObjectId=doc.getId();
            Map<String, Object> docContent = new HashMap<String, Object>();

            docContent.put("patient",encryptorRND.encrypt(patient));
            //tags to search by
            docContent.put("ssn",encryptorDET.encrypt(patient.SSN));
            docContent.put("visitdate", encryptorOPE.encrypt(patient.VisitDate));

            doc.putProperties(docContent);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return "";
        }
        return doc.getId();
    }
    public Patient getPatientBySSN(String SSN) {
        Query query = database.getView("ssn").createQuery();
        ArrayList<Object> ssns = new ArrayList<>();
        ssns.add(encryptorDET.encrypt(SSN));
        query.setKeys(ssns);
        query.setLimit(1);

        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        if (result != null && result.hasNext()) {
            QueryRow row = result.next();
            Document doc = row.getDocument();
            Map<String, Object> docProperties = doc.getProperties();
            String patientEncrypted= (String)docProperties.get("patient");

            return (Patient)encryptorRND.decrypt(patientEncrypted,Patient.class);
        }
        return null;
    }
    public List<Patient> getPatientsVisitedBetween(Date start, Date end) {
        Query query = database.getView("visitdate").createQuery();
        query.setStartKey(encryptorOPE.encrypt(start));
        query.setEndKey(encryptorOPE.encrypt(end));
        query.setLimit(10);

        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        ArrayList<Patient> patients = new ArrayList<>();
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            Document doc = row.getDocument();
            Map<String, Object> docContent = doc.getProperties();
            Patient patient = (Patient) encryptorRND.decrypt((String) docContent.get("patient"), Patient.class);
            patients.add(patient);
        }
        return patients;
    }

}

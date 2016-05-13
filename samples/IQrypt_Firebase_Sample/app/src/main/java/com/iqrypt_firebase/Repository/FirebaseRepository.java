package com.iqrypt_firebase.Repository;

import android.content.Context;
import android.util.Log;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.client.realtime.util.StringListReader;
import com.iqrypt.Cipher;
import com.iqrypt.EncryptionType;
import com.iqrypt.EncryptorFactory;
import com.iqrypt.IEncryptor;
import com.iqrypt.IQryptConfigurator;
import com.iqrypt_firebase.Repository.models.Patient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




public class FirebaseRepository {
   private Firebase patients;

    IEncryptor encryptorRND;
    IEncryptor encryptorDET ;
    IEncryptor encryptorOPE ;
    public FirebaseRepository()
    {

        Firebase myFirebaseRef = new Firebase("https://iqryptdemo.firebaseio.com/");
        patients=myFirebaseRef.child("patients");
        //init IQrypt
        IQryptConfigurator.setEncryptionChiper(Cipher.AES256, "my_super_secret");
        encryptorRND = EncryptorFactory.getEncryptor(EncryptionType.RND);
        encryptorDET = EncryptorFactory.getEncryptor(EncryptionType.DET);
        encryptorOPE = EncryptorFactory.getEncryptor(EncryptionType.OPE);
    }
    public String createPatient(Patient patient) {

        Firebase newPatientRef = patients.push();


        patient.ObjectId = newPatientRef.getKey();
        Map<String, String> docContent = new HashMap<String, String>();

        docContent.put("patient", encryptorRND.encrypt(patient));
        //tags to search by
        docContent.put("ssn", encryptorDET.encrypt(patient.SSN));
        docContent.put("visitdate", encryptorOPE.encrypt(patient.VisitDate));

        newPatientRef.setValue(docContent);

        Log.d("LOG_PATIENT", "Patient with id:" + patient.ObjectId + " created");
        return newPatientRef.getKey();
    }
    public void  getPatientBySSN(String SSN) {
        String SSNEncrypted = encryptorDET.encrypt(SSN);
        Query queryRef = patients.orderByChild("ssn").equalTo(SSNEncrypted);


        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                String patientEncrypted=snapshot.child("patient").getValue(String.class);
                Patient pDec= (Patient) encryptorRND.decrypt(patientEncrypted,Patient.class);
                Log.d("LOG_PATIENT_BY_SSN", "Patient with SSN:" + pDec.SSN +" loaded" );
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


    }
    public void getPatientsVisitedBetween(Date start, Date end) {
        String encryptedStart=encryptorOPE.encrypt(start);
        String encryptedEnd=encryptorOPE.encrypt(end);
        Query queryRef = patients.orderByChild("visitdate").startAt(encryptedStart).endAt(encryptedEnd);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                String patientEncrypted=snapshot.child("patient").getValue(String.class);
                Patient pDec= (Patient) encryptorRND.decrypt(patientEncrypted,Patient.class);
                Log.d("LOG_PATIENT_BTW_DATES", "Patient with VisitDate:" + pDec.VisitDate +" loaded" );
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}

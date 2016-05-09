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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SimpleRepository {
    private Database database;
    private android.content.Context ctx;
    private Manager manager;
    IEncryptor encryptorRND;
    IEncryptor encryptorDET ;
    IEncryptor encryptorOPE ;
    public SimpleRepository(Context context)
    {
        this.ctx=context;
        try {
            manager = new Manager(new AndroidContext(ctx), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
           e.printStackTrace();
            return;
        }
        try {

            database = manager.getDatabase("mysimpledb");
            database.delete();
            database = manager.getDatabase("mysimpledb");
            // Create a view and register its map function:
            View emailView = database.getView("email");
            if (emailView.getMap() == null) {
                emailView.setMap(new Mapper() {
                    @Override
                    public void map(Map<String, Object> document, Emitter emitter) {

                        emitter.emit(document.get("email"), null);

                    }

                }, "2");
            }
            View scoreView = database.getView("score");
            if (scoreView.getMap() == null) {
                scoreView.setMap(new Mapper() {
                    @Override
                    public void map(Map<String, Object> document, Emitter emitter) {

                        emitter.emit(document.get("score"), null);

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


    public String create(Map<String, Object> docContent) {

        Document doc = database.createDocument();
        // add content to document and write the document to the database
        try {
            //hooking
            //encrypt email with DET so we can make equality queries
            String email=(String)docContent.get("email");
            String emailEncrypted=encryptorDET.encrypt(email);
            docContent.put("email",emailEncrypted);

            //encrypt info with RND scheme
            String info=(String)docContent.get("info");
            String infoEncrypted=encryptorRND.encrypt(info);
            docContent.put("info",infoEncrypted);

            //encrypt score with OPE scheme so we can make range queries
            int score=(int)docContent.get("score");
            String scoreEncrypted=encryptorOPE.encrypt(score);
            docContent.put("score", scoreEncrypted);

            doc.putProperties(docContent);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return "";
        }
        return doc.getId();
    }
    public Map<String, Object>  getDocByEmail(String email) {
        Query query = database.getView("email").createQuery();

        String emailEncrypted=encryptorDET.encrypt(email);

        ArrayList<Object> emails = new ArrayList<>();
        emails.add(emailEncrypted);
        query.setKeys(emails);

        query.setLimit(1);

        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        if ( result!=null && result.hasNext() ) {
            QueryRow row = result.next();
            Document doc = row.getDocument();
            Map<String, Object> docContentUmodifiable=doc.getProperties();
            HashMap<String,Object> docContent=new HashMap<>(docContentUmodifiable);
            //decrypt

            String emailEnc=(String)docContent.get("email");
            String emailDec=(String)encryptorDET.decrypt(emailEnc, String.class);
            docContent.put("email",emailDec);

            String infoEnc=(String)docContent.get("info");
            String infoDec=(String)encryptorRND.decrypt(infoEnc, String.class);
            docContent.put("info",infoDec);

            String scoreEnc=(String)docContent.get("score");
            Long scoreDec=(Long)encryptorOPE.decrypt(scoreEnc, Long.class);
            docContent.put("score",scoreDec);

            return docContent;

        }
        return null;

    }
    public List<Map<String,Object>> getAllDocsBetweenScore(int start,int end) {
        Query query = database.getView("score").createQuery();

        String startEnc=encryptorOPE.encrypt(start);
        query.setStartKey(startEnc);

        String endEnc=encryptorOPE.encrypt(end);
        query.setEndKey(endEnc);

        query.setLimit(20);

        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        ArrayList<Map<String,Object>> docs = new ArrayList<>();
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            Document doc = row.getDocument();
            Map<String, Object> docContentUmodifiable=doc.getProperties();
            HashMap<String,Object> docContent=new HashMap<>(docContentUmodifiable);
            //decrypt

            String emailEnc=(String)docContent.get("email");
            String emailDec=(String)encryptorDET.decrypt(emailEnc, String.class);
            docContent.put("email",emailDec);

            String infoEnc=(String)docContent.get("info");
            String infoDec=(String)encryptorRND.decrypt(infoEnc, String.class);
            docContent.put("info",infoDec);

            String scoreEnc=(String)docContent.get("score");
            Long scoreDec=(Long)encryptorOPE.decrypt(scoreEnc, Long.class);
            docContent.put("score",scoreDec);

            docs.add(docContent);
        }
        return docs;

    }
    public boolean delete(String docId) {


        Document doc = null;
        // delete the document
        try {
            doc = database.getDocument(docId);
            doc.delete();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();;
        }
        return doc.isDeleted();
    }
}

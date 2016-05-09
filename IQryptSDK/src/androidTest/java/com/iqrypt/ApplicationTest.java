package com.iqrypt;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;


import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    Random rnd = new Random();
    static boolean init = false;

    @Test
    public void testAA() {

        assertEquals(true, true);
    }

    @Test
    public void testDET() throws Exception {
        TestDET(Cipher.Camellia256, "TestDetC256");
        TestDET(Cipher.Camellia128, "TestDetC128");
        TestDET(Cipher.AES128, "TestDetA128");
        TestDET(Cipher.AES256, "TestDetA256");

    }

    private void TestDET(Cipher cipher, String className) throws Exception {
        IQryptConfigurator.resetSettings();



        IQryptConfigurator.setEncryptionChiper(cipher, "Myaskfhakdjfh_superKey");
        IEncryptor encryptor = EncryptorFactory.getEncryptor(EncryptionType.DET);

        int IntVal = rnd.nextInt();
        String intEnc = encryptor.encrypt(IntVal);
        assertEquals(IntVal, encryptor.decrypt(intEnc, int.class));

        double f = ((double) IntVal) * 1.55f;
        String fEnc = encryptor.encrypt(f);

        double diff = (f - (double)encryptor.decrypt(fEnc, double.class));
        assertTrue( (diff < 1 && diff>0) || (diff>-1 && diff<0) || diff==0);

        String s = "AbrameÉ" + IntVal;
        String sEnc = encryptor.encrypt(s);
        assertEquals(s, encryptor.decrypt(sEnc, String.class));


        MyPOCO p = new MyPOCO();
        p.MyInt = IntVal;
        p.MyString = "sadas" + IntVal;

        String pEnc = encryptor.encrypt(p);

        assertEquals(IntVal, ((MyPOCO) encryptor.decrypt(pEnc, MyPOCO.class)).MyInt);
        assertEquals("sadas" + IntVal, ((MyPOCO) encryptor.decrypt(pEnc, MyPOCO.class)).MyString);

        //equality check
        String intEnc2 = encryptor.encrypt(IntVal);
        assertEquals(intEnc, intEnc2);

        String fEnc2 = encryptor.encrypt(f);
        assertEquals(fEnc, fEnc2);

        String sEnc2 = encryptor.encrypt(s);
        assertEquals(sEnc, sEnc2);

        String pEnc2 = encryptor.encrypt(p);
        assertEquals(pEnc, pEnc2);

    }

    @Test
    public void testRND() throws Exception {
        TestRND(Cipher.Camellia256, "TestsRND");
        TestRND(Cipher.Camellia128, "TestsRND");
        TestRND(Cipher.AES128, "TestsRND");
        TestRND(Cipher.AES256, "TestsRND");

    }

    private void TestRND(Cipher cipher, String className) throws Exception {
        IQryptConfigurator.resetSettings();



        IQryptConfigurator.setEncryptionChiper(cipher, "Myaskfhakdjfh_superKey");
        IEncryptor encryptor = EncryptorFactory.getEncryptor(EncryptionType.RND);

        int IntVal = rnd.nextInt();
        String intEnc = encryptor.encrypt(IntVal);
        assertEquals(IntVal, encryptor.decrypt(intEnc, int.class));

        double f = ((double) IntVal) * 1.55f;
        String fEnc = encryptor.encrypt(f);
        double diff = (f - (double)encryptor.decrypt(fEnc, double.class));
        assertTrue( (diff < 1 && diff>0) || (diff>-1 && diff<0) || diff==0);

        String s = "AbrameÉ" + IntVal;
        String sEnc = encryptor.encrypt(s);
        assertEquals(s, encryptor.decrypt(sEnc, String.class));


        MyPOCO p = new MyPOCO();
        p.MyInt = IntVal;
        p.MyString = "sadas" + IntVal;

        String pEnc = encryptor.encrypt(p);

        assertEquals(IntVal, ((MyPOCO) encryptor.decrypt(pEnc, MyPOCO.class)).MyInt);
        assertEquals("sadas" + IntVal, ((MyPOCO) encryptor.decrypt(pEnc, MyPOCO.class)).MyString);

        //equality check
        String intEnc2 = encryptor.encrypt(IntVal);
        assertNotSame(intEnc, intEnc2);

        String fEnc2 = encryptor.encrypt(f);
        assertNotSame(fEnc, fEnc2);

        String sEnc2 = encryptor.encrypt(s);
        assertNotSame(sEnc, sEnc2);

        String pEnc2 = encryptor.encrypt(p);
        assertNotSame(pEnc, pEnc2);

    }

    @Test
    public void testOPE() throws Exception {
        IQryptConfigurator.resetSettings();



        IQryptConfigurator.setEncryptionChiper(Cipher.Camellia256, "Myaskfhakdjfh_superKey");
        IEncryptor encryptor = EncryptorFactory.getEncryptor(EncryptionType.OPE);

        int IntVal = rnd.nextInt(100000);
        String intEnc = encryptor.encrypt(IntVal);
        assertEquals((long) IntVal, encryptor.decrypt(intEnc, int.class));

        float f = ((float) IntVal) * 1.55f;
        String fEnc = encryptor.encrypt(f);

        double diff= f - (float) encryptor.decrypt(fEnc, float.class);
        assertTrue( (diff < 1 && diff>0) || (diff>-1 && diff<0) || diff==0);

        Date now = new Date();
        String dEnc = encryptor.encrypt(now);


        Calendar calExpected = Calendar.getInstance();
        calExpected.setTime(now);
        Calendar calActual = Calendar.getInstance();
        calActual.setTime((Date) encryptor.decrypt(dEnc, Date.class));
        assertEquals(calExpected.get(Calendar.YEAR), calActual.get(Calendar.YEAR));
        assertEquals(calExpected.get(Calendar.MONTH), calActual.get(Calendar.MONTH));
        assertEquals(calExpected.get(Calendar.DAY_OF_MONTH), calActual.get(Calendar.DAY_OF_MONTH));
        assertEquals(calExpected.get(Calendar.HOUR), calActual.get(Calendar.HOUR));
        assertEquals(calExpected.get(Calendar.MINUTE), calActual.get(Calendar.MINUTE));
        assertEquals(calExpected.get(Calendar.SECOND), calActual.get(Calendar.SECOND));


        //equality check
        String intEnc2 = encryptor.encrypt(IntVal);
        assertEquals(intEnc, intEnc2);

        String fEnc2 = encryptor.encrypt(f);
        assertEquals(fEnc, fEnc2);

        String dEnc2 = encryptor.encrypt(now);
        assertEquals(dEnc, dEnc2);

        //bigger
        String intEncB = encryptor.encrypt(IntVal + 1);
        assertTrue(intEnc.compareTo(intEncB) < 0);

        float f2 = f + 1f;
        String fEncB = encryptor.encrypt(f2);
        assertTrue(fEnc.compareTo(fEncB) < 0);

        Calendar calB = Calendar.getInstance();
        calB.setTime(now);
        calB.add(Calendar.YEAR, 1);
        Date dateF = calB.getTime();
        String dEncB = encryptor.encrypt(dateF);
        assertTrue(dEnc.compareTo(dEncB) < 0);

        //less
        String intEncL = encryptor.encrypt(IntVal - 1);
        assertTrue(intEnc.compareTo(intEncL) > 0);

        float f3 = f - 1f;
        String fEncL = encryptor.encrypt(f3);
        assertTrue(fEnc.compareTo(fEncL) > 0);

        Calendar calL = Calendar.getInstance();
        calL.setTime(now);
        calL.add(Calendar.YEAR, -1);
        Date dateF2 = calL.getTime();
        String dEncL = encryptor.encrypt(dateF2);
        assertTrue(dEnc.compareTo(dEncL) > 0);
    }
    @Test
    public void testROPE() throws Exception {
        IQryptConfigurator.resetSettings();



        IQryptConfigurator.setEncryptionChiper(Cipher.Camellia256, "Myaskfhakdjfh_superKey");
        IEncryptor encryptor = EncryptorFactory.getEncryptor(EncryptionType.ROPE);

        int IntVal = rnd.nextInt(100000);
        String intEnc = encryptor.encrypt(IntVal);
        assertEquals((long) IntVal, encryptor.decrypt(intEnc, int.class));

        float f = ((float) IntVal) * 1.55f;
        String fEnc = encryptor.encrypt(f);

        double diff= f - (float) encryptor.decrypt(fEnc, float.class);
        assertTrue( (diff < 1 && diff>0) || (diff>-1 && diff<0) || diff==0);

        Date now = new Date();
        String dEnc = encryptor.encrypt(now);


        Calendar calExpected = Calendar.getInstance();
        calExpected.setTime(now);
        Calendar calActual = Calendar.getInstance();
        calActual.setTime((Date) encryptor.decrypt(dEnc, Date.class));
        assertEquals(calExpected.get(Calendar.YEAR), calActual.get(Calendar.YEAR));
        assertEquals(calExpected.get(Calendar.MONTH), calActual.get(Calendar.MONTH));
        assertEquals(calExpected.get(Calendar.DAY_OF_MONTH), calActual.get(Calendar.DAY_OF_MONTH));
        assertEquals(calExpected.get(Calendar.HOUR), calActual.get(Calendar.HOUR));
        assertEquals(calExpected.get(Calendar.MINUTE), calActual.get(Calendar.MINUTE));
        assertEquals(calExpected.get(Calendar.SECOND), calActual.get(Calendar.SECOND));

        //probability tests

        //equality check
        String intEnc2 = encryptor.encrypt(IntVal);
        assertNotSame(intEnc, intEnc2);

        String fEnc2 = encryptor.encrypt(f);
        assertNotSame(fEnc, fEnc2);

        String dEnc2 = encryptor.encrypt(now);
        assertNotSame(dEnc, dEnc2);

        //bigger
        String intEncB = encryptor.encrypt(IntVal + 1);
        assertTrue(intEnc.compareTo(intEncB) < 0);

        float f2 = f + 1f;
        String fEncB = encryptor.encrypt(f2);
        assertTrue(fEnc.compareTo(fEncB) < 0);

        Calendar calB = Calendar.getInstance();
        calB.setTime(now);
        calB.add(Calendar.YEAR, 1);
        Date dateF = calB.getTime();
        String dEncB = encryptor.encrypt(dateF);
        assertTrue(dEnc.compareTo(dEncB) < 0);

        //less
        String intEncL = encryptor.encrypt(IntVal - 1);
        assertTrue(intEnc.compareTo(intEncL) > 0);

        float f3 = f - 1f;
        String fEncL = encryptor.encrypt(f3);
        assertTrue(fEnc.compareTo(fEncL) > 0);

        Calendar calL = Calendar.getInstance();
        calL.setTime(now);
        calL.add(Calendar.YEAR, -1);
        Date dateF2 = calL.getTime();
        String dEncL = encryptor.encrypt(dateF2);
        assertTrue(dEnc.compareTo(dEncL) > 0);
    }
    @Test
    public void testHMAC() throws Exception {
        IQryptConfigurator.resetSettings();
        IQryptConfigurator.setEncryptionChiper(Cipher.AES256, "Myaskfhakdjfh_superKey");
        IEncryptor encryptor = EncryptorFactory.getEncryptor(EncryptionType.HMAC);

        int IntVal =rnd.nextInt();
        String intEnc = encryptor.encrypt(IntVal);
        boolean exc = false;
        try
        {
            encryptor.decrypt(intEnc, int.class);
        }
        catch (RuntimeException ex)
        {
         if(ex.getMessage()=="You cannot decrypt with HMAC") {
             exc = true;
         }
        }
        assertTrue(exc);

        double f = ((double)IntVal) * 1.55f;
        String fEnc = encryptor.encrypt(f);
        exc = false;
        try
        {
            encryptor.decrypt(fEnc, double.class);
        }
        catch (RuntimeException ex)
        {
            if(ex.getMessage()=="You cannot decrypt with HMAC") {
                exc = true;
            }
        }
        assertTrue(exc);

        String s = "AbrameÉ" + IntVal;
        String sEnc = encryptor.encrypt(s);
        exc = false;
        try
        {
            encryptor.decrypt(sEnc, String.class);
        }
        catch (RuntimeException ex)
        {
            if(ex.getMessage()=="You cannot decrypt with HMAC") {
                exc = true;
            }
        }
        assertTrue(exc);


        //equality check
        String intEnc2 = encryptor.encrypt(IntVal);
        assertEquals(intEnc, intEnc2);

        String fEnc2 = encryptor.encrypt(f);
        assertEquals(fEnc, fEnc2);

        String sEnc2 = encryptor.encrypt(s);
        assertEquals(sEnc, sEnc2);

    }

}
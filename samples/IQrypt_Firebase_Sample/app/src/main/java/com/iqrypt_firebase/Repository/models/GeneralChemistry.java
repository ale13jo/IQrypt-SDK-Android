package com.iqrypt_firebase.Repository.models;

/**
 * Created by Cristi on 3/23/2016.
 */ // This class contains patient's general chemistry information
public class GeneralChemistry
{
    public String Na;
    public String K;

    public String Glucose;

    public String Creatine;
    public String BUN;
    public static GeneralChemistry Example()
    {
        GeneralChemistry generalChemistry= new GeneralChemistry();
        generalChemistry.Na = "56 mEq/L";
        generalChemistry.K = "23 mEq/L";
        generalChemistry.Glucose = "26 mg/dl";
        generalChemistry.Creatine = "13 mg/dl";
        generalChemistry.BUN = "78 mg/dl";
        return generalChemistry;
    }
}

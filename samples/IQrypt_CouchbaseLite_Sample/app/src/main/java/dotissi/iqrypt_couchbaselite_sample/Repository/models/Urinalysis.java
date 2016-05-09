package dotissi.iqrypt_couchbaselite_sample.Repository.models;

/**
 * Created by Cristi on 3/23/2016.
 */ // This class contains patient's urinalysis data
public class Urinalysis
{
    public String Protien;

    public String Sugar;
    public String OccultBlood;
    public static Urinalysis Example()
    {
        Urinalysis urinalysis= new Urinalysis();
        urinalysis.Protien = "(-)";
        urinalysis.Sugar = "(+)";
        urinalysis.OccultBlood = "(-)";
        return urinalysis;
    }
}

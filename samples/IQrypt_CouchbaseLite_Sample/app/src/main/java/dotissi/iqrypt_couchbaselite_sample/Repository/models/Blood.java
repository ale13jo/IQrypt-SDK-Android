package dotissi.iqrypt_couchbaselite_sample.Repository.models;

/**
 * Created by Cristi on 3/23/2016.
 */ // This class contains patient's blood data
public  class Blood
{
    public String Hemoglobin;

    public String Hematocrit;
    public String WBC;
    public String Platelets;
    public static Blood Example()
    {
        Blood blood= new Blood();
        blood.Hemoglobin = "45 gb/dll";
        blood.Hematocrit = "23%";
        blood.WBC = "12 ul";
        blood.Platelets= "34/mcl";
        return blood;
    }
}

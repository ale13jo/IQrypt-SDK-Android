package dotissi.iqrypt_couchbaselite_sample.Repository.models;

/**
 * Created by Cristi on 3/23/2016.
 */
public class BloodGas
{
    public String pH;

    public String pCO2;

    public String HCO3;

    public String AG;

    public String p02;
    public static BloodGas Example()
    {
        BloodGas bloodGas= new BloodGas();
        bloodGas.pH = "7.32";
        bloodGas.pCO2 = "34";
        bloodGas.HCO3 = "26";
        bloodGas.AG = "13";
        bloodGas.p02="78";
        return bloodGas;
    }

}

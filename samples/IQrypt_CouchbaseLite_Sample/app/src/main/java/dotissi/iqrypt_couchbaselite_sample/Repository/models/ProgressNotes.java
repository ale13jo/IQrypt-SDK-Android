package dotissi.iqrypt_couchbaselite_sample.Repository.models;

/**
 * Created by Cristi on 3/23/2016.
 */ // This class contains notes about patient
public class ProgressNotes
{

    public String Month;

    public String Date;

    public String Note;

    // Constructor
    public ProgressNotes(String month, String date, String note)
    {
        this.Month = month;
        this.Date = date;
        this.Note = note;
    }
    public ProgressNotes(){}
}

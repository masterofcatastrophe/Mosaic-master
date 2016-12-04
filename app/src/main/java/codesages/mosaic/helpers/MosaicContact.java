package codesages.mosaic.helpers;

import java.util.Date;

/**
 * Created by DELL on 18/11/2016.
 */

public class MosaicContact {
    String Name;
    Date LastCall;
    String Frequency;
    int FrequencyInDays;
    ContactNumbers contactNumbers;

    public MosaicContact(String name, ContactNumbers contactnumbers) {
        Name = name;
        contactNumbers = contactnumbers;
        this.FrequencyInDays = 2;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getLastCall() {
        return LastCall;
    }

    public void setLastCall(Date lastCall) {
        LastCall = lastCall;
    }

    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        Frequency = frequency;
    }

    public ContactNumbers getContactNumbers() {
        return contactNumbers;
    }

    public void setContactNumbers(ContactNumbers contactNumbers) {
        this.contactNumbers = contactNumbers;
    }

    public int getFrequencyInDays() {
        return FrequencyInDays;
    }

    public void setFrequencyInDays(int frequencyInDays) {
        FrequencyInDays = frequencyInDays;
    }
}

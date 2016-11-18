package codesages.mosaic.helpers;

import java.util.Date;

/**
 * Created by DELL on 18/11/2016.
 */

public class MosaicContact {
    String Name;
    Date LastCall;
    String Email;
    String Number;
    String Period;
    boolean isEmail;

    public MosaicContact(String name, String email, String number, boolean isEmail, String period) {
        Name = name;
        Email = email;
        Number = number;
        this.isEmail = isEmail;
        Period = period;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public boolean isEmail() {
        return isEmail;
    }

    public void setEmail(boolean email) {
        isEmail = email;
    }
}

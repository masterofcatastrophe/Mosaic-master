package codesages.mosaic.helpers;

import java.util.ArrayList;

/**
 * Created by DELL on 10/11/2016.
 */

public class ContactNumbers {

    ArrayList<String> numbers = new ArrayList<>();
    ArrayList<String> Emails = new ArrayList<>();

    public ContactNumbers(ArrayList<String> numbers, ArrayList<String> emails) {
        this.numbers = numbers;
        Emails = emails;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public ArrayList<String> getEmails() {
        return Emails;
    }

    public void setEmails(ArrayList<String> emails) {
        Emails = emails;
    }

    public ArrayList<String> getNumbersEmail() {

        ArrayList<String> list = new ArrayList<>(this.getNumbers());
        for (int i = 0; i < this.Emails.size(); i++) {
            list.add("Email: " + this.Emails.get(i));
        }


        return list;
    }
}

package codesages.mosaic.helpers;

import java.util.ArrayList;

/**
 * Created by DELL on 10/11/2016.
 */

public class ContactNumbers {
    String Phone = "";
    String Work = "";
    String Email = "";
    String Home = "";
    ArrayList<String> numbers = new ArrayList<>();

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

    public String getHome() {
        return Home;
    }

    public void setHome(String home) {
        Home = home;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getWork() {
        return Work;
    }

    public void setWork(String work) {
        Work = work;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

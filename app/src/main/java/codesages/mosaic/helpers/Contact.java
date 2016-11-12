package codesages.mosaic.helpers;

/**
 * Created by DELL on 10/11/2016.
 */

public class Contact {
    String Name;
    String Id;
    ContactNumbers numbers;

    public Contact(String name, String id) {
        Name = name;
        Id = id;
    }
    public ContactNumbers getNumbers() {
        return numbers;
    }

    public void setNumbers(ContactNumbers numbers) {
        this.numbers = numbers;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}

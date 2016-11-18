package codesages.mosaic.helpers;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DELL on 12/11/2016.
 */

public class Mosaic {
    String Name;
    Date CreatedOn;
    Bitmap bitmap;
    ImagePath imagePath;
    ArrayList<MosaicContact> contacts;

    public Mosaic(String name, Date createdOn, Bitmap bitmap) {
        Name = name;
        CreatedOn = createdOn;
        this.bitmap = bitmap;
    }

    public Mosaic(String name, Date createdOn, ImagePath imagePath) {
        Name = name;
        CreatedOn = createdOn;
        this.imagePath = imagePath;
    }

    public ArrayList<MosaicContact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<MosaicContact> contacts) {
        this.contacts = contacts;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        CreatedOn = createdOn;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImagePath getImagePath() {
        return imagePath;
    }

    public void setImagePath(ImagePath imagePath) {
        this.imagePath = imagePath;
    }
}

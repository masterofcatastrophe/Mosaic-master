package codesages.mosaic.helpers;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

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
    ArrayList<MosaicContact> contacts = new ArrayList<>();

    public Mosaic() {
    }

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


    public static class FindMosaic {
        public int mosaicIndex;
        public int contactIndex;
        static String TAG = "FindMosaic";

        public FindMosaic(int mosaicIndex, int contactIndex) {
            this.mosaicIndex = mosaicIndex;
            this.contactIndex = contactIndex;
        }

        public static FindMosaic findMosaic(ArrayList<Mosaic> mosaics, String numberToFind) {
            Log.d(TAG, "findMosaic: ");
            for (int i = 0; i < mosaics.size(); i++) {
                Mosaic mosaic = mosaics.get(i);
                ArrayList<MosaicContact> mosaicConntacts = mosaic.getContacts();
                for (int j = 0; j < mosaicConntacts.size(); j++) {
                    //Log.d(TAG, "findMosaic: Contact: " + mosaicConntacts.get(j).getName());
                    ArrayList<String> contactNumber = mosaic.getContacts().get(j).getContactNumbers().getNumbers();
                    ArrayList<String> contactEmails = mosaic.getContacts().get(j).getContactNumbers().getEmails();
                    for (int k = 0; k < contactNumber.size(); k++) {
                        //Log.d(TAG, "findMosaic: Numbers: " + contactNumber.get(k));
                        if (contactNumber.get(k).replaceAll("[- ]+", "")
                                .contains(numberToFind.replaceAll("[- ]+", ""))
                                || numberToFind.replaceAll("[- ]+", "")
                                .contains(contactNumber.get(k).replaceAll("[- ]+", ""))) {
                            Log.d(TAG, "findMosaic: MATCH-> " + contactNumber.get(k) + " AND " + (numberToFind));
                            return new FindMosaic(i, j);
                        }
                    }
                    for (int k = 0; k < contactEmails.size(); k++) {
                        //Log.d(TAG, "findMosaic: Numbers: " + contactEmails.get(k));
                        if (contactEmails.get(k).replaceAll("\\s+", "")
                                .contains(numberToFind.replaceAll("\\s+", ""))
                                || numberToFind.replaceAll("\\s+", "")
                                .contains(contactEmails.get(k).replaceAll("\\s+", ""))) {
                            Log.d(TAG, "findMosaic: MATCH-> " + contactEmails.get(k) + " AND " + (numberToFind));
                            return new FindMosaic(i, j);
                        }
                    }
                }
            }
            return null;
        }

        public static FindMosaic findMosaicbyContactName(ArrayList<Mosaic> mosaics, String contactName) {
            Log.d(TAG, "findMosaicbyContactName: ");
            for (int i = 0; i < mosaics.size(); i++) {
                Mosaic mosaic = mosaics.get(i);
                ArrayList<MosaicContact> mosaicConntacts = mosaic.getContacts();
                for (int j = 0; j < mosaicConntacts.size(); j++) {
                    //Log.d(TAG, "findMosaic: Contact: " + mosaicConntacts.get(j).getName());
                    if (mosaicConntacts.get(j).getName().contains(contactName)
                            || contactName.contains(mosaicConntacts.get(j).getName())) {
                        Log.d(TAG, "findMosaicbyContactName: MATCH FOUND " + mosaicConntacts.get(j).getName() + " AND " + contactName);
                        return new FindMosaic(i, j);
                    }
                }
            }
            return null;
        }
    }
}

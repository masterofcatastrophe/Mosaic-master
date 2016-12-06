package codesages.mosaic.helpers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import codesages.mosaic.R;

/**
 * Created by DELL on 06/12/2016.
 */

public class NotificationsHelper {
    static String TAG = "NotificationsHelper";

    public static ArrayList<MosaicNotification> getNotificationsStrings(Context ctx) {
        ArrayList<Mosaic> mosaics = CacheManager.getMosaicFromCache(ctx);
        ArrayList<MosaicNotification> mosaicNotifications = new ArrayList<>();

        //loop through the mosaics
        for (int i = 0; i < mosaics.size(); i++) {
            Mosaic mosaic = mosaics.get(i);
            ArrayList<MosaicContact> mosaicContact = mosaic.getContacts();
            String strings = "";
            Log.d(TAG, "getNotificationsStrings: " + mosaic.getName());
            //loop through Contacts of the Mosaic
            for (int j = 0; j < mosaicContact.size(); j++) {

                long daysDiff = TimeUnit
                        .DAYS
                        .convert(new Date().getTime()
                                        - mosaicContact.get(i).getLastCall().getTime(),
                                TimeUnit.MILLISECONDS);
                //if not contacted according to the frequency add to list
                if (daysDiff > mosaicContact.get(i).getFrequencyInDays()) {
                    strings += mosaicContact.get(i).getName() + "\n";
                    Log.d(TAG, "getNotificationsStrings: " + strings);
                }
            }
            if (!strings.isEmpty())
                mosaicNotifications.add(new MosaicNotification(mosaic.getName(), strings));

        }
        return mosaicNotifications;
    }

    public static class MosaicNotification {
        String MosaicName;
        String contactNames;

        public MosaicNotification(String mosaicName, String contactNames) {
            MosaicName = mosaicName;
            this.contactNames = contactNames;
        }

        public String getMosaicName() {
            return MosaicName;
        }

        public String getContactNames() {
            return contactNames;
        }
    }
}

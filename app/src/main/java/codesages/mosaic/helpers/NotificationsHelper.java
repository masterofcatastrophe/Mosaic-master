package codesages.mosaic.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import codesages.mosaic.MosaicApplication;
import codesages.mosaic.R;
import codesages.mosaic.receiver.AlarmReceiver;

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
                if (mosaicContact.get(j).getLastCall() != null) {
                    long daysDiff = TimeUnit
                            .DAYS
                            .convert(new Date().getTime()
                                            - mosaicContact.get(j).getLastCall().getTime(),
                                    TimeUnit.MILLISECONDS);
                    //if not contacted according to the frequency add to list
                    if (daysDiff > mosaicContact.get(j).getFrequencyInDays()) {
                        strings += mosaicContact.get(j).getName() + "\n";
                        Log.d(TAG, "getNotificationsStrings: " + strings);
                    }
                } else {
                    strings += mosaicContact.get(j).getName() + "\n";
                }
            }
            if (!strings.isEmpty())
                mosaicNotifications.add(new MosaicNotification(mosaic.getName(), strings));
        }
        return mosaicNotifications;
    }

    public static void startAlarms(Context ctx) {

        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar9Am = Calendar.getInstance();
        calendar9Am.setTimeInMillis(System.currentTimeMillis());
        calendar9Am.set(Calendar.HOUR_OF_DAY, 9);
        calendar9Am.set(Calendar.MINUTE, 0);
        calendar9Am.set(Calendar.SECOND, 0);
        Calendar calendar2Pm = Calendar.getInstance();
        calendar2Pm.setTimeInMillis(System.currentTimeMillis());
        calendar2Pm.set(Calendar.HOUR_OF_DAY, 14);
        calendar2Pm.set(Calendar.MINUTE, 00);
        calendar2Pm.set(Calendar.SECOND, 0);
        Calendar calendar6Pm = Calendar.getInstance();
        calendar6Pm.setTimeInMillis(System.currentTimeMillis());
        calendar6Pm.set(Calendar.HOUR_OF_DAY, 18);
        calendar6Pm.set(Calendar.MINUTE, 00);
        calendar6Pm.set(Calendar.SECOND, 0);


        /* Repeating every day interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar9Am.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2Pm.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar6Pm.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
        CacheManager.setAlarmsSet(ctx, true);

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

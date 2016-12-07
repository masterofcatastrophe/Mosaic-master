package codesages.mosaic;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.ArrayList;
import java.util.Calendar;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Contact;
import codesages.mosaic.receiver.AlarmReceiver;
import codesages.mosaic.receiver.OutgoingSMSObserver;

/**
 * Created by DELL on 27/11/2016.
 */

public class MosaicApplication extends Application {
    public static ArrayList<Contact> contacts;
    static final Uri SMS_STATUS_URI = Uri.parse("content://sms");
    static String TAG = "MosaicApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
        startAlarms();
        startougoingSMSObserver();
    }

    private void startAlarms() {
        if (!CacheManager.isAlarmsSet(this)) {
            Intent alarmIntent = new Intent(MosaicApplication.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MosaicApplication.this, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar9Am = Calendar.getInstance();
            calendar9Am.setTimeInMillis(System.currentTimeMillis());
            calendar9Am.set(Calendar.HOUR_OF_DAY, 11);
            calendar9Am.set(Calendar.MINUTE, 25);
            calendar9Am.set(Calendar.SECOND, 0);
            Calendar calendar2Pm = Calendar.getInstance();
            calendar2Pm.setTimeInMillis(System.currentTimeMillis());
            calendar2Pm.set(Calendar.HOUR_OF_DAY, 11);
            calendar2Pm.set(Calendar.MINUTE, 25);
            calendar2Pm.set(Calendar.SECOND, 0);
            Calendar calendar6Pm = Calendar.getInstance();
            calendar6Pm.setTimeInMillis(System.currentTimeMillis());
            calendar6Pm.set(Calendar.HOUR_OF_DAY, 11);
            calendar6Pm.set(Calendar.MINUTE, 25);
            calendar6Pm.set(Calendar.SECOND, 0);


        /* Repeating every day interval */
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar9Am.getTimeInMillis(),
                    1000 * 60 * 60 * 24, pendingIntent);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2Pm.getTimeInMillis(),
                    1000 * 60 * 60 * 24, pendingIntent);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar6Pm.getTimeInMillis(),
                    1000 * 60 * 60 * 24, pendingIntent);
            CacheManager.setAlarmsSet(this, true);
        }
    }

    public void startougoingSMSObserver() {
        Log.d(TAG, "startougoingSMSObserver: ");
        OutgoingSMSObserver smsSentObserver = new OutgoingSMSObserver(this, new Handler());
        getContentResolver().registerContentObserver(SMS_STATUS_URI, true, smsSentObserver);
        CacheManager.setOugoingSMSObserverFlag(this, true);

    }
}

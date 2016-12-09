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
import codesages.mosaic.helpers.NotificationsHelper;
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
        if (!CacheManager.isAlarmsSet(this)) {
            NotificationsHelper.startAlarms(this);
        }
        startougoingSMSObserver();
    }


    public void startougoingSMSObserver() {
        Log.d(TAG, "startougoingSMSObserver: ");
        OutgoingSMSObserver smsSentObserver = new OutgoingSMSObserver(this, new Handler());
        getContentResolver().registerContentObserver(SMS_STATUS_URI, true, smsSentObserver);
        CacheManager.setOugoingSMSObserverFlag(this, true);

    }
}

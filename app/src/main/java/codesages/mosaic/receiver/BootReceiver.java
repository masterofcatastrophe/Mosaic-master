package codesages.mosaic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.NotificationsHelper;

public class BootReceiver extends BroadcastReceiver {
    static String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: BOOT");
        CacheManager.setAlarmsSet(context, false);
        NotificationsHelper.startAlarms(context);
    }
}

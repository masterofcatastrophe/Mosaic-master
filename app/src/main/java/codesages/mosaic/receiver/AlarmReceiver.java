package codesages.mosaic.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import codesages.mosaic.R;
import codesages.mosaic.helpers.NotificationsHelper;
import codesages.mosaic.splashScreen;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    static String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + new Date());
        buildNotification(context);

    }

    private void buildNotification(Context context) {
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        new Intent(context, splashScreen.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        ArrayList<NotificationsHelper.MosaicNotification> list = NotificationsHelper.getNotificationsStrings(context);
        for (int i = 0; i < list.size(); i++) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.ic_popup_reminder)
                            .setContentTitle(list.get(i).getMosaicName().toUpperCase())
                            .setContentText("Contacts require some attention!")
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(list.get(i).getContactNames()))
                            .setContentIntent(resultPendingIntent);

            mNotifyMgr.notify(001 + i, mBuilder.build());
        }
    }

}
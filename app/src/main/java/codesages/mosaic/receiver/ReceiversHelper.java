package codesages.mosaic.receiver;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import codesages.mosaic.mosaicsListScreen;

/**
 * Created by DELL on 21/11/2016.
 */

public class ReceiversHelper {
    static String TAG = "ReceiversHelper";

    public static void enableInComingSMSReceiver(Context ctx, boolean enableIncomnig) {
        Log.d(TAG, "enableInComingSMSReceiver: " + enableIncomnig);
        int flagIn = (enableIncomnig ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);

        ComponentName inComponent = new ComponentName(ctx, IncomingSMSReceiver.class);

        ctx.getPackageManager().setComponentEnabledSetting(
                inComponent,
                flagIn,
                PackageManager.DONT_KILL_APP
        );

    }

    public static void enableInComingReceiver(Context ctx, boolean enableIncomnig) {
        Log.d(TAG, "enableInComingReceiver: " + enableIncomnig);
        int flagIn = (enableIncomnig ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);

        ComponentName inComponent = new ComponentName(ctx, IncomingCallsReceiver.class);

        ctx.getPackageManager().setComponentEnabledSetting(
                inComponent,
                flagIn,
                PackageManager.DONT_KILL_APP
        );

    }

    public static void enableOutgoingReceiver(Context ctx, boolean enableOutgoing) {
        Log.d(TAG, "enableOutgoingReceiver: " + enableOutgoing);

        int flagOut = (enableOutgoing ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED);

        ComponentName outComponent = new ComponentName(ctx, OutgoingCallsReceiver.class);
        ctx.getPackageManager().setComponentEnabledSetting(
                outComponent,
                flagOut,
                PackageManager.DONT_KILL_APP
        );
    }
}

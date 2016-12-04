package codesages.mosaic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Mosaic;

import static codesages.mosaic.helpers.Mosaic.FindMosaic.findMosaic;
import static codesages.mosaic.helpers.Mosaic.FindMosaic.findMosaicbyContactName;

public class IncomingSMSReceiver extends BroadcastReceiver {

    static String TAG = "IncomingSMSReceiver";
    final SmsManager sms = SmsManager.getDefault();
    Context ctx;

    public IncomingSMSReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        ctx = context;
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = getIncomingMessage(pdusObj[i], bundle);
                    String phoneNumberDispaly = currentMessage.getDisplayOriginatingAddress();

                    //String message = currentMessage.getDisplayMessageBody();
                    Log.d("SmsReceiver", "senderNum: Display " + ": " + phoneNumberDispaly);
                    processNumber(phoneNumberDispaly);
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception " + e);
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }

    public void processNumber(String contactName) {
        Log.d(TAG, "processNumber: ");
        ArrayList<Mosaic> mosaics = CacheManager.getMosaicFromCache(ctx);
        ArrayList<Mosaic.FindMosaic> list1 = findMosaicbyContactName(mosaics, contactName);
        for (Mosaic.FindMosaic findMosaic : list1) {
            if (findMosaic != null) {
                CacheManager.updateMosaicContact(ctx,
                        findMosaic.mosaicIndex,
                        findMosaic.contactIndex,
                        new Date());
            }
        }
        ArrayList<Mosaic.FindMosaic> list2 = findMosaic(mosaics, contactName);
        for (Mosaic.FindMosaic findMosaic : list2) {
            if (findMosaic != null) {
                CacheManager.updateMosaicContact(ctx,
                        findMosaic.mosaicIndex,
                        findMosaic.contactIndex,
                        new Date());
            }
        }
    }
}

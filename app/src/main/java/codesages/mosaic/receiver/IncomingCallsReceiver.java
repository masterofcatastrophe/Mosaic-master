package codesages.mosaic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Mosaic;

import static codesages.mosaic.helpers.Mosaic.FindMosaic.findMosaic;

public class IncomingCallsReceiver extends BroadcastReceiver {
    Context ctx;
    static String TAG = "IncomingCallsReceiver";

    public IncomingCallsReceiver() {
    }

    @Override
    public void onReceive(Context mContext, Intent intent) {
        ctx = mContext;
        try {
            // TELEPHONY MANAGER class object to register one listner
            TelephonyManager tmgr = (TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);

            //Create Listner
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

            // Register listener for LISTEN_CALL_STATE
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }


    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {
            Mosaic.FindMosaic findMosaic;
            Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);
            if (state == (TelephonyManager.CALL_STATE_RINGING)) {
                //Toast.makeText(ctx, "Phone Is Ringing ", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCallStateChanged: " + TelephonyManager.EXTRA_STATE_RINGING);
            }

            if (state == (TelephonyManager.CALL_STATE_OFFHOOK)) {
                //Toast.makeText(ctx, "Call Recieved", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCallStateChanged: " + TelephonyManager.EXTRA_STATE_OFFHOOK);

                if (!incomingNumber.equals("") && !incomingNumber.isEmpty())
                    processNumber(incomingNumber);

            }

            if (state == (TelephonyManager.CALL_STATE_IDLE)) {
                //Toast.makeText(ctx, "Phone Is Idle", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onCallStateChanged: " + TelephonyManager.EXTRA_STATE_IDLE);
            }
        }

    }

    public void processNumber(String number) {
        Log.d(TAG, "processNumber: ");
        ArrayList<Mosaic> mosaics = CacheManager.getMosaicFromCache(ctx);
        ArrayList<Mosaic.FindMosaic> list = findMosaic(mosaics, number);
        for (Mosaic.FindMosaic findMosaic : list) {
            if (findMosaic != null) {
                CacheManager.updateMosaicContact(ctx,
                        findMosaic.mosaicIndex,
                        findMosaic.contactIndex,
                        new Date());
            }
        }

    }
}



package codesages.mosaic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Mosaic;

import static codesages.mosaic.helpers.Mosaic.FindMosaic.findMosaic;

public class OutgoingCallsReceiver extends BroadcastReceiver {
    public OutgoingCallsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Log.d("OutgoingCallsReceiver", "RINGING (outgoing call) with number: " + number);
        Toast.makeText(context, "Outgoing call number: " + number, Toast.LENGTH_LONG).show();
        processNumber(context, number);
    }

    public void processNumber(Context ctx, String number) {
        Log.d("OutgoingCallsReceiver", "processNumber: ");
        ArrayList<Mosaic> mosaics = CacheManager.getMosaicFromCache(ctx);
        Mosaic.FindMosaic findMosaic = findMosaic(mosaics, number);
        if (findMosaic != null) {
            CacheManager.updateMosaicContact(ctx,
                    findMosaic.mosaicIndex,
                    findMosaic.contactIndex,
                    new Date());
        }
    }


}

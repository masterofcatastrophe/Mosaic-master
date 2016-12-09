package codesages.mosaic.receiver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import codesages.mosaic.helpers.CacheManager;
import codesages.mosaic.helpers.Mosaic;
import codesages.mosaic.helpers.MosaicContact;

import static codesages.mosaic.helpers.Mosaic.FindMosaic.findMosaic;
import static codesages.mosaic.helpers.Mosaic.FindMosaic.findMosaicbyContactName;

/**
 * Created by DELL on 21/11/2016.
 */

public class OutgoingSMSObserver extends ContentObserver {

    private static final String TAG = "OutgoingSMSObserver";
    Context context;
    Handler handler;

    public OutgoingSMSObserver(Context c, Handler handler) {
        super(handler);

        this.context = c;
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Uri outMMS = Uri.parse("content://sms/sent");
        //Desc descending ASC in ascending order
        Cursor cursor = context.getContentResolver().query(outMMS, null, null, null, "date DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {

                String number = cursor.getString(cursor.getColumnIndex("address"));
                Log.d(TAG, "onChange: " + "The sender :" + number);
                //Log.d(TAG, "onChange: " + "Message: " + cursor.getString(cursor.getColumnIndex("body")) + "\n");
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))));
                Log.d(TAG, "onChange: " + "Date: " + date + "\n");
                if (processNumber(date, number))
                    break;
            }
            cursor.close();
        }
    }

    public boolean processNumber(Date date, String number) {
        ArrayList<Mosaic> mosaics = CacheManager.getMosaicFromCache(context);
        ArrayList<Mosaic.FindMosaic> list1 = findMosaicbyContactName(mosaics, number);
        for (Mosaic.FindMosaic findMosaic : list1) {
            if (findMosaic != null) {
                MosaicContact mContact = mosaics.get(findMosaic.mosaicIndex).getContacts().get(findMosaic.contactIndex);
                if (mContact.getLastCall() != null) {
                    if (mContact.getLastCall().compareTo(date) < 0) {
                        CacheManager.updateMosaicContact(context,
                                findMosaic.mosaicIndex,
                                findMosaic.contactIndex,
                                date);
                        return true;
                    }
                } else {
                    CacheManager.updateMosaicContact(context,
                            findMosaic.mosaicIndex,
                            findMosaic.contactIndex,
                            date);
                    return true;
                }
            }
        }

        ArrayList<Mosaic.FindMosaic> list2 = findMosaic(mosaics, number);
        for (Mosaic.FindMosaic findMosaic : list2) {
            if (findMosaic != null) {
                MosaicContact mContact = mosaics.get(findMosaic.mosaicIndex).getContacts().get(findMosaic.contactIndex);
                if (mContact.getLastCall() != null) {
                    if (mContact.getLastCall().compareTo(date) < 0) {
                        CacheManager.updateMosaicContact(context,
                                findMosaic.mosaicIndex,
                                findMosaic.contactIndex,
                                date);
                        return true;
                    }
                } else {
                    CacheManager.updateMosaicContact(context,
                            findMosaic.mosaicIndex,
                            findMosaic.contactIndex,
                            date);
                    return true;
                }
            }
        }
        return false;

    }

}
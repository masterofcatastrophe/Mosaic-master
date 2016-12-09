package codesages.mosaic.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DELL on 12/11/2016.
 * <p>
 * __  .___________.    _______.
 * |  | |           |   /       |
 * |  | `---|  |----`  |   (----`
 * |  |     |  |        \   \
 * |  |     |  |    .----)   |
 * |__|     |__|    |_______/
 *
 *  ______     ___       ______  __    __   _______
 * /      |   /   \     /      ||  |  |  | |   ____|
 * |  ,----'  /  ^  \   |  ,----'|  |__|  | |  |__
 * |  |      /  /_\  \  |  |     |   __   | |   __|
 * |  `----./  _____  \ |  `----.|  |  |  | |  |____
 * \______/__/     \__\ \______||__|  |__| |_______|
 */

public class CacheManager {
    static String TAG = "CacheManager";
    public static String MOSAIC_SHARED_PREFERENCE = "mosaic_prefs";
    public static String MOSAIC_LIST = "mosaic_list";
    public static String OBSERVER_FLAG = "observer_flag";
    public static String ALARMS_FLAG = "alarms_flag";

    public static boolean getOugoingSMSObserverFlag(Context ctx) {

        SharedPreferences sharedpref = ctx.getSharedPreferences(MOSAIC_SHARED_PREFERENCE, 0);
        boolean flag = sharedpref.getBoolean(OBSERVER_FLAG, false);
        Log.d(TAG, "getOugoingSMSObserverFlag: " + flag);
        return flag;
    }

    public static void setOugoingSMSObserverFlag(Context ctx, boolean isEnabled) {
        SharedPreferences sharedpref = ctx.getSharedPreferences(MOSAIC_SHARED_PREFERENCE, 0);
        sharedpref.edit().putBoolean(OBSERVER_FLAG, isEnabled).commit();
    }

    public static ArrayList<Mosaic> getMosaicFromCache(Context ctx) {
        Log.d(TAG, "getMosaicFromCache: ");
        SharedPreferences sharedpref = ctx.getSharedPreferences(MOSAIC_SHARED_PREFERENCE, 0);
        String json = sharedpref.getString(MOSAIC_LIST, "");
        if (!json.equals("")) {
            Gson gson = new Gson();
            ArrayList<Mosaic> mos = gson.fromJson(json, new TypeToken<ArrayList<Mosaic>>() {
            }.getType());
            Log.d(TAG, "Size" + mos.size());
            return mos;
        }
        return new ArrayList<Mosaic>();
    }

    public static Mosaic getMosaicByPosition(Context ctx, int index) {
        Log.d(TAG, "getMosaicByPosition: " + index);
        ArrayList<Mosaic> mosaics = getMosaicFromCache(ctx);

        return mosaics.size() > index ? mosaics.get(index) : null;
    }

    public static boolean updateMosaicByPosition(Context ctx, Mosaic mosiac, int position) {
        Log.d(TAG, "updateMosaicByPosition: " + position);
        ArrayList<Mosaic> mosaics = getMosaicFromCache(ctx);
        if (mosaics.size() > position) {
            mosaics.set(position, mosiac);
            setMosaicFromCache(ctx, mosaics);
            return true;
        }
        return false;

    }

    public static boolean deleteMosaicByPosition(Context ctx, int position) {
        Log.d(TAG, "deleteMosaicByPosition: " + position);
        ArrayList<Mosaic> mosaics = getMosaicFromCache(ctx);
        if (mosaics.size() > position) {
            mosaics.remove(position);
            setMosaicFromCache(ctx, mosaics);
            return true;
        }
        return false;

    }

    public static void setMosaicFromCache(Context ctx, ArrayList<Mosaic> list) {
        Log.d(TAG, "setMosaicFromCache: ");
        Gson gson = new Gson();
        String json = gson.toJson(list);
        SharedPreferences sharedpref = ctx.getSharedPreferences(MOSAIC_SHARED_PREFERENCE, 0);
        sharedpref.edit().putString(MOSAIC_LIST, json).commit();
    }

    public static void addToMosaicFromCache(Context ctx, Mosaic mosaic) {
        Log.d(TAG, "addToMosaicFromCache: Adding " + mosaic.getName());
        ArrayList<Mosaic> cachedlist = getMosaicFromCache(ctx);
        Log.d(TAG, "addToMosaicFromCache: Cached Size = " + cachedlist.size());
        cachedlist.add(mosaic);
        setMosaicFromCache(ctx, cachedlist);

    }

    public static boolean addMosaicContact(Context ctx, int mosaicIndex, MosaicContact mosaicContact) {
        ArrayList<MosaicContact> contacts = new ArrayList<>();
        Mosaic mosaic = getMosaicByPosition(ctx, mosaicIndex);
        if (mosaic == null)
            return false;

        if (mosaic.getContacts() != null)
            contacts = mosaic.getContacts();
        else
            contacts = new ArrayList<>();

        //contacts.add(mosaicContact);

        mosaic.setContacts(mergeWithCached(contacts, mosaicContact));
        updateMosaicByPosition(ctx, mosaic, mosaicIndex);
        return true;

    }

    public static boolean updateMosaicContact(Context ctx, int mosaicIndex, int contactIndex, Date lastContactedOn) {
        Log.d(TAG, "updateMosaicContact: M.index: " + mosaicIndex + " C.index: " + contactIndex);
        ArrayList<Mosaic> mosaics = getMosaicFromCache(ctx);
        if (mosaics.size() > mosaicIndex) {
            if (mosaics.get(mosaicIndex).getContacts().size() > contactIndex) {
                mosaics.get(mosaicIndex).getContacts().get(contactIndex).setLastCall(lastContactedOn);
                updateMosaicByPosition(ctx, mosaics.get(mosaicIndex), mosaicIndex);
                return true;
            }
        }
        return false;
    }

    public static boolean updateMosaicContact(Context ctx, int mosaicIndex, int contactIndex, int frequence) {
        Log.d(TAG, "updateMosaicContact: M.index: " + mosaicIndex + " C.index: " + contactIndex);
        ArrayList<Mosaic> mosaics = getMosaicFromCache(ctx);
        if (mosaics.size() > mosaicIndex) {
            if (mosaics.get(mosaicIndex).getContacts().size() > contactIndex) {
                mosaics.get(mosaicIndex).getContacts().get(contactIndex).setFrequencyInDays(frequence);
                updateMosaicByPosition(ctx, mosaics.get(mosaicIndex), mosaicIndex);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteMosaicContact(Context ctx, int mosaicIndex, int contactIndex) {
        Log.d(TAG, "deleteMosaicContact: M.index: " + mosaicIndex + " C.index: " + contactIndex);
        ArrayList<Mosaic> mosaics = getMosaicFromCache(ctx);
        if (mosaics.size() > mosaicIndex) {
            if (mosaics.get(mosaicIndex).getContacts().size() > contactIndex) {
                mosaics.get(mosaicIndex).getContacts().remove(contactIndex);
                updateMosaicByPosition(ctx, mosaics.get(mosaicIndex), mosaicIndex);
                return true;
            }
        }
        return false;
    }

    public static MosaicContact getMosaicContact(Context ctx, int mosaicIndex, int contactIndex) {
        ArrayList<Mosaic> mosaics = getMosaicFromCache(ctx);
        if (mosaics.size() > mosaicIndex) {
            if (mosaics.get(mosaicIndex).getContacts().size() > contactIndex) {
                return mosaics.get(mosaicIndex).getContacts().get(contactIndex);

            }
        }
        return null;
    }

    public static ArrayList<MosaicContact> mergeWithCached(ArrayList<MosaicContact> contacts, MosaicContact mosaicContact) {
        MosaicContact cachedMosaicContact = null;
        boolean nameFound = false;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getName().equals(mosaicContact.getName())) {
                nameFound = true;
                //Log.d(TAG, "mergeWithCached: " + contacts.get(i).getName() + "==" + mosaicContact.getName());
                cachedMosaicContact = contacts.get(i);

                //Log.d(TAG, "mergeWithCached: Loop Numbers");
                for (int k = 0; k < mosaicContact.getContactNumbers().getNumbers().size(); k++) {
                    //Log.d(TAG, "mergeWithCached: " + mosaicContact.getName());
                    boolean innerFound = false;
                    String neW = mosaicContact.getContactNumbers().getNumbers().get(k);
                    //Log.d(TAG, "mergeWithCached: New Number" + neW);
                    for (int j = 0; j < cachedMosaicContact.getContactNumbers().getNumbers().size(); j++) {
                        String cached = cachedMosaicContact.getContactNumbers().getNumbers().get(j);
                        //Log.d(TAG, "mergeWithCached: Cached Number" + cached);
                        if (neW.equals(cached)) {
                            //Log.d(TAG, "mergeWithCached: EQUAL");
                            innerFound = true;
                            break;
                        }
                    }
                    if (!innerFound) {
                        //Log.d(TAG, "mergeWithCached: ADD new to Cached");
                        contacts.get(i).getContactNumbers().getNumbers().add(neW);

                    }
                }
                //Log.d(TAG, "mergeWithCached: Loop Emails");
                for (int k = 0; k < mosaicContact.getContactNumbers().getEmails().size(); k++) {
                    //Log.d(TAG, "mergeWithCached: " + mosaicContact.getName());
                    boolean innerFound = false;
                    String neW = mosaicContact.getContactNumbers().getEmails().get(k);
                    //Log.d(TAG, "mergeWithCached: New Email" + neW);
                    for (int j = 0; j < cachedMosaicContact.getContactNumbers().getEmails().size(); j++) {
                        String cached = cachedMosaicContact.getContactNumbers().getEmails().get(j);
                        //Log.d(TAG, "mergeWithCached: Cached Email" + cached);
                        if (neW.equals(cached)) {
                            //Log.d(TAG, "mergeWithCached: EQUAL");
                            innerFound = true;
                            break;
                        }
                    }
                    if (!innerFound) {
                        //Log.d(TAG, "mergeWithCached: ADD new to Cached");
                        contacts.get(i).getContactNumbers().getEmails().add(neW);

                    }
                }
            }
        }
        if (!nameFound) {
            //Log.d(TAG, "mergeWithCached: Name not found");
            contacts.add(mosaicContact);
        }
        return contacts;

    }

    public static void setAlarmsSet(Context ctx, boolean flag) {
        SharedPreferences sharedpref = ctx.getSharedPreferences(MOSAIC_SHARED_PREFERENCE, 0);
        sharedpref.edit().putBoolean(ALARMS_FLAG, flag).commit();
        Log.d(TAG, "setAlarmsSet: " + flag);
    }

    public static boolean isAlarmsSet(Context ctx) {
        Log.d(TAG, "isAlarmsSet: ");
        SharedPreferences sharedpref = ctx.getSharedPreferences(MOSAIC_SHARED_PREFERENCE, 0);
        return sharedpref.getBoolean(ALARMS_FLAG, false);

    }
}

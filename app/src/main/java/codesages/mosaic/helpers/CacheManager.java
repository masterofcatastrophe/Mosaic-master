package codesages.mosaic.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by DELL on 12/11/2016.
 * <p>
 * __  .___________.    _______.
 * |  | |           |   /       |
 * |  | `---|  |----`  |   (----`
 * |  |     |  |        \   \
 * |  |     |  |    .----)   |
 * |__|     |__|    |_______/
 * <p>
 * _______  __    __    ______  __  ___  __  .__   __.   _______
 * |   ____||  |  |  |  /      ||  |/  / |  | |  \ |  |  /  _____|
 * |  |__   |  |  |  | |  ,----'|  '  /  |  | |   \|  | |  |  __
 * |   __|  |  |  |  | |  |     |    <   |  | |  . `  | |  | |_ |
 * |  |     |  `--'  | |  `----.|  .  \  |  | |  |\   | |  |__| |
 * |__|      \______/   \______||__|\__\ |__| |__| \__|  \______|
 * <p>
 * ______     ___       ______  __    __   _______
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
}

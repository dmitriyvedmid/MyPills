package com.dmitriyvedmid.mypills;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dmitr on 5/29/2017.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "pillsPref";

    private static final String PREV_START_DIVIDED_BY_1000 = "prevStart";
    private static final String PREV_LAST_DIVIDED_BY_1000 = "prevLast";
    private static final String PREV_INTERVAL = "prevInterval";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setPrevStart(long start) {
        editor.putLong(PREV_START_DIVIDED_BY_1000, start);
        editor.commit();
    }
    public long getPrevStart() {
        return pref.getLong(PREV_START_DIVIDED_BY_1000, System.currentTimeMillis());
    }

    public void setPrevLast(long last) {
        editor.putLong(PREV_LAST_DIVIDED_BY_1000, last);
        editor.commit();
    }
    public long getPrevLast() {
        return pref.getLong(PREV_LAST_DIVIDED_BY_1000, 5);
    }

    public void setPrevInterval(long interval) {
        editor.putLong(PREV_INTERVAL, interval);
        editor.commit();
    }
    public long getPrevInterval() {
        return pref.getLong(PREV_INTERVAL, 28);
    }
}
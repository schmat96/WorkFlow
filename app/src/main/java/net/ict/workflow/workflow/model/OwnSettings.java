package net.ict.workflow.workflow.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class OwnSettings {

    public static String KEY_WEEKS = "DayOfWeek";

    public static int getDaysCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> someStringSet = new HashSet<String>();
        someStringSet = prefs.getStringSet(OwnSettings.KEY_WEEKS, someStringSet);
        return 113;
    }

}

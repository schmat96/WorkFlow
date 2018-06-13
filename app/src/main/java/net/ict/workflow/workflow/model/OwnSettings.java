package net.ict.workflow.workflow.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import net.ict.workflow.workflow.Converter;
import net.ict.workflow.workflow.R;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class OwnSettings {

    public static String KEY_WEEKS = "DayOfWeek";
    public static String WORKING_MINUTES = "WorkingMinutes";

    public static int getDaysCode() {
        Boolean[] weekDays = getWeeks();
        return Converter.getIntWeekDays(weekDays);
    }

    public static Boolean[] getWeeks() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        Set<String> someStringSet = new HashSet<String>();
        someStringSet = prefs.getStringSet(OwnSettings.KEY_WEEKS, someStringSet);
        String[] dayOfWeeks = App.getContext().getResources().getStringArray(R.array.daysOfWeek);
        Boolean[] weeks = new Boolean[dayOfWeeks.length];
        for (int i = 0;i<weeks.length;i++) {
            if (someStringSet.contains(dayOfWeeks[i])) {
                weeks[i] = true;
            } else {
                weeks[i] = false;
            }
        }
        return weeks;
    }

    public static float getTimePerDay() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        int time = 480;
        String s = prefs.getString(OwnSettings.WORKING_MINUTES, "default");
        try {
            time = Integer.parseInt(s);
        } finally {
            return (float) ((float) time/ (float)60);
        }

    }
}

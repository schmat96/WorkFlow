package net.ict.workflow.workflow.model;

import android.util.Log;

import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BadgeTimes {
    ArrayList<LocalDateTime> times;

    private static final String TAG = "MyActivity";

    public BadgeTimes() {
        times = new ArrayList<>();
    }

    public void init() {
        for (int i = 0; i<20;i++) {
            LocalDateTime ldt = LocalDateTime.now().plusHours(i);
            times.add(ldt);
        }
    }

    public ArrayList<LocalDateTime> getTimeStampsInDate(LocalDate date) {
        ArrayList<LocalDateTime> result = new ArrayList<>();
        Iterator<LocalDateTime> iter = times.iterator();
        while (iter.hasNext()) {
            LocalDateTime ts = iter.next();
            boolean sameDay = (date.getDayOfYear()==ts.getDayOfYear() && date.getYear()==ts.getYear());
            if (sameDay) {
                result.add(ts);
            }

        }
        return result;
    }
}

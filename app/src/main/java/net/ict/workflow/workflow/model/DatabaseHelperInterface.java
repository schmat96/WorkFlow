package net.ict.workflow.workflow.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ict.workflow.workflow.Converter;

import java.time.LocalDateTime;
import java.util.TreeSet;

public interface DatabaseHelperInterface {


    // --------------------------------------------------------------------------
    // INSERT STATEMENTS

    // INSERT BADGETIMES
    public long insertBadgeTime(LocalDateTime localDateTime, float hours, long daysCode);

    // INSERT HOURS_DAY
    public long insertHoursPerDay(float hours);

    // --------------------------------------------------------------------------
    // SELECT STATEMENTS

    // SELECT BADGETIMES
    public TreeSet<LocalDateTime> getAllBadgeTimes();

    public float getBadgeTimeMax(LocalDateTime localDateTime);

    public long getHoursPerDayId(float hours, SQLiteDatabase db);

    public long getBadgeTimeDaySet(LocalDateTime localDateTime);

    public int getBadgeTimesCount();

    // SELECT HOURS_DAY
    public float getHoursPerDay(long id);

    public int getHoursPerDayCount();

    // --------------------------------------------------------------------------
    // UPDATE STATEMENTS

    // UPDATE BADGETIMES
    public int updateBadgeTimes(LocalDateTime localDateTimeOld, LocalDateTime localDateTimeNew);

    // --------------------------------------------------------------------------
    // DELETE STATEMENTS

    // DELETE BADGETIMES
    public void deleteBadgeTimes(LocalDateTime localDateTime);
}

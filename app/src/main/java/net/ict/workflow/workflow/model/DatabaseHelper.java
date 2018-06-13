package net.ict.workflow.workflow.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.ict.workflow.workflow.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "BadgeTimes.db";

    // badge times statements
    private static final String TABLE_BADGETIMES = "badgetimes";
    private static final String ATTR_TIME = "time";
    private static final String ATTR_HOURS_DAY_FK = "hours_day_id";
    private static final String ATTR_DAYS_WEEK = "days_week";

    private static final String CREATE_TABLE_BADGETIMES = "CREATE TABLE " + TABLE_BADGETIMES + " ("
            + ATTR_TIME + " TEXT PRIMARY KEY, " + ATTR_HOURS_DAY_FK + " INTEGER, " + ATTR_DAYS_WEEK
            + " INTEGER)";
    private static final String DROP_TABLE_BADGETIMES = "DROP TABLE IF EXISTS " + TABLE_BADGETIMES;

    // max hours per day statements
    private static final String TABLE_HOURS_DAY = "hours_day";
    private static final String ATTR_ID_HOURS_DAY = "id_hours_day";
    private static final String ATTR_MAX = "max";

    private static final String CREATE_TABLE_HOURS_DAY = "CREATE TABLE " + TABLE_HOURS_DAY + " ("
            + ATTR_ID_HOURS_DAY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ATTR_MAX + " REAL)";
    private static final String DROP_TABLE_HOURS_DAY = "DROP TABLE IF EXISTS " + TABLE_HOURS_DAY;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BADGETIMES);
        db.execSQL(CREATE_TABLE_HOURS_DAY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_BADGETIMES);
        db.execSQL(DROP_TABLE_HOURS_DAY);
        onCreate(db);
    }

    public void onDownGrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // --------------------------------------------------------------------------
    // INSERT STATEMENTS

    // INSERT BADGETIMES
    public long insertBadgeTime(LocalDateTime localDateTime, long hoursId, long daysCode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ATTR_TIME, Converter.localDateTimeToString(localDateTime));
        values.put(ATTR_HOURS_DAY_FK, hoursId);
        values.put(ATTR_DAYS_WEEK, daysCode);

        long currentId = db.insert(TABLE_BADGETIMES, null, values);
        // TODO ugly, re-enable once fixed
        db.close();

        return currentId;
    }

    // INSERT HOURS_DAY
    public long insertHoursPerDay(float hours) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ATTR_MAX, hours);

        long currentId = db.insert(TABLE_HOURS_DAY, null, values);
        // TODO ugly, re-enable once fixed
        db.close();

        return currentId;
    }

    // --------------------------------------------------------------------------
    // SELECT STATEMENTS

    // SELECT BADGETIMES
    public TreeSet<LocalDateTime> getAllBadgeTimes() {
        SQLiteDatabase db = this.getReadableDatabase();
        TreeSet<LocalDateTime> allBadgeTimes = new TreeSet<>();

        String selectQuery = "SELECT * FROM " + TABLE_BADGETIMES;

        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                String time = c.getString(c.getColumnIndex(ATTR_TIME));
                allBadgeTimes.add(Converter.stringToLocalDateTime(time));
            } while (c.moveToNext());
        }
        c.close();
        // TODO ugly, re-enable once fixed
        db.close();

        return allBadgeTimes;
    }

    public float getBadgeTimeMax(LocalDateTime localDateTime) {
        SQLiteDatabase db = this.getReadableDatabase();
/*
        String selectQuery = "SELECT * FROM " + TABLE_BADGETIMES + " WHERE " + ATTR_TIME
                + " = '" + localDateTime.toString() + "'";
*/
        String selectQuery = "SELECT * FROM " + TABLE_BADGETIMES;

        Cursor c = db.rawQuery(selectQuery, null);
        float dailyMax = 5.24f;

        if (c != null && c.moveToFirst()) {
            dailyMax = c.getFloat(c.getColumnIndex(ATTR_HOURS_DAY_FK));
            c.close();
        }

        // TODO ugly, re-enable once fixed
        db.close();

        return dailyMax;
    }

    public long getBadgeTimeDaySet(LocalDateTime localDateTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_BADGETIMES + " WHERE " + ATTR_TIME
                + " = '" + localDateTime.toString() + "'";

        Cursor c = db.rawQuery(selectQuery, null);
        long daySetCode = 0;
        if (c != null && c.moveToFirst()) {
            daySetCode = c.getInt(c.getColumnIndex(ATTR_DAYS_WEEK));
            c.close();
        }

        // TODO ugly, re-enable once fixed
        db.close();

        return daySetCode;
    }

    public int getBadgeTimesCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + TABLE_BADGETIMES;

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        // TODO ugly, re-enable once fixed
        db.close();

        return count;
    }

    // SELECT HOURS_DAY
    public float getHoursPerDay(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_HOURS_DAY + " WHERE " + ATTR_ID_HOURS_DAY
                + " = " + id;

        float dailyMax = 0;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null && c.moveToFirst()) {
            dailyMax = c.getFloat(c.getColumnIndex(ATTR_MAX));
            c.close();
        }

        // TODO ugly, re-enable once fixed
        db.close();

        return dailyMax;
    }

    public int getHoursPerDayCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + TABLE_HOURS_DAY;

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        // TODO ugly, re-enable once fixed
        db.close();

        return count;
    }

    // --------------------------------------------------------------------------
    // UPDATE STATEMENTS

    // UPDATE BADGETIMES
    public int updateBadgeTimes(LocalDateTime localDateTimeOld, LocalDateTime localDateTimeNew) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ATTR_TIME, localDateTimeNew.toString());

        int id = db.update(TABLE_BADGETIMES, values, ATTR_TIME + " = ?",
                new String[] { String.valueOf(localDateTimeOld)});

        // TODO ugly, re-enable once fixed
        db.close();

        return id;
    }

    // --------------------------------------------------------------------------
    // DELETE STATEMENTS

    // DELETE BADGETIMES
    public void deleteBadgeTimes(LocalDateTime localDateTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_BADGETIMES, ATTR_TIME +  " = ?",
                new String[] { String.valueOf(localDateTime)});

        // TODO ugly, re-enable once fixed
        db.close();
    }
}

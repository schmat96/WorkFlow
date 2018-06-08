package net.ict.workflow.workflow.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BadgeTimes.db";

    // badge times statements
    private static final String TABLE_BADGETIMES = "badgetimes";
    private static final String ATTR_TIME = "time";
    private static final String ATTR_HOURS_DAY_FK = "hours_day_id";
    private static final String ATTR_DAYS_WEEK = "days_week";

    private static final String CREATE_TABLE_BADGETIMES = "CREATE TABLE " + TABLE_BADGETIMES + " ("
            + ATTR_TIME + " TEXT PRIMARY KEY, " + ATTR_HOURS_DAY_FK + " INT, " + ATTR_DAYS_WEEK
            + " INT)";
    private static final String DROP_TABLE_BADGETIMES = "DROP TABLE IF EXISTS " + TABLE_BADGETIMES;

    // max hours per day statements
    private static final String TABLE_HOURS_DAY = "hours_day";
    private static final String ATTR_ID_HOURS_DAY = "id_hours_day";
    private static final String ATTR_MAX = "max";

    private static final String CREATE_TABLE_HOURS_DAY = "CREATE TABLE " + TABLE_HOURS_DAY + " ("
            + ATTR_ID_HOURS_DAY + " INT PRIMARY KEY AUTOINCREMENT, " + ATTR_MAX + " REAL)";
    private static final String DROP_TABLE_HOURS_DAY = "DROP TABLE IF EXISTS " + TABLE_HOURS_DAY;

    // TODO delete this once safe to do so
    /*
    // days per week statements
    private static final String TABLE_DAYS_WEEK = "days_week";
    private static final String ATTR_ID_DAYS_WEEK = "id_days_week";
    private static final String ATTR_MONDAY = "monday";
    private static final String ATTR_TUESDAY = "tuesday";
    private static final String ATTR_WEDNESDAY = "wednesday";
    private static final String ATTR_THURSDAY = "thursday";
    private static final String ATTR_FRIDAY = "friday";
    private static final String ATTR_SATURDAY = "saturday";
    private static final String ATTR_SUNDAY = "sunday";


    private static final String CREATE_TABLE_DAYS_WEEK = "CREATE TABLE " + TABLE_DAYS_WEEK + " ("
            + ATTR_ID_DAYS_WEEK + " INT PRIMARY KEY AUTOINCREMENT, " + ATTR_MONDAY + " INT, " + ATTR_TUESDAY
            + " INT, " + ATTR_WEDNESDAY + " INT, " + ATTR_THURSDAY + " INT, " + ATTR_FRIDAY + " INT, "
            + ATTR_SATURDAY + " INT, " + ATTR_SUNDAY + " INT)";
    private static final String DROP_TABLE_DAYS_WEEK = "DROP TABLE IF EXISTS " + TABLE_DAYS_WEEK;
    */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BADGETIMES);
        db.execSQL(CREATE_TABLE_HOURS_DAY);
        // TODO delete this once safe to do so
        //db.execSQL(CREATE_TABLE_DAYS_WEEK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_BADGETIMES);
        db.execSQL(DROP_TABLE_HOURS_DAY);
        // TODO delete this once safe to do so
        //db.execSQL(DROP_TABLE_DAYS_WEEK);
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
        values.put(ATTR_TIME, localDateTime.toString());
        values.put(ATTR_HOURS_DAY_FK, hoursId);
        values.put(ATTR_DAYS_WEEK, daysCode);

        long currentId = db.insert(TABLE_BADGETIMES, null, values);
        db.close();

        return currentId;
    }

    // INSERT HOURS_DAY
    public long insertHoursPerDay(float hours) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ATTR_MAX, hours);

        long currentId = db.insert(TABLE_HOURS_DAY, null, values);
        db.close();

        return currentId;
    }

    // TODO delete this once safe to do so
    // possibly going to need to update this...
    /*
    // INSERT DAYS_WEEK
    public long insertDaysPerWeek(boolean[] days) {
        SQLiteDatabase db = this.getWritableDatabase();

        int[] ints;
        ints = getBoolToInt(days);
        ContentValues values = new ContentValues();
        values.put(ATTR_MONDAY, ints[0]);
        values.put(ATTR_TUESDAY, ints[1]);
        values.put(ATTR_WEDNESDAY, ints[2]);
        values.put(ATTR_THURSDAY, ints[3]);
        values.put(ATTR_FRIDAY, ints[4]);
        values.put(ATTR_SATURDAY, ints[5]);
        values.put(ATTR_SUNDAY, ints[6]);

        long currentId = db.insert(TABLE_DAYS_WEEK, null, values);
        db.close();

        return currentId;
    }
    */

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
                DateTimeFormatter formatter;
                formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
                allBadgeTimes.add(LocalDateTime.parse(time, formatter));
            } while (c.moveToNext());
        }
        c.close();
        db.close();

        return allBadgeTimes;
    }

    public long getBadgeTimeMax(LocalDateTime localDateTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_BADGETIMES + " WHERE " + ATTR_TIME
                + " = " + localDateTime.toString();

        Cursor c = db.rawQuery(selectQuery, null);
        long dailyMax = 0;
        if (c != null && c.moveToFirst()) {
            dailyMax = c.getInt(c.getColumnIndex(ATTR_HOURS_DAY_FK));
            c.close();
        }

        db.close();

        return dailyMax;
    }

    public long getBadgeTimeDaySet(LocalDateTime localDateTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_BADGETIMES + " WHERE " + ATTR_TIME
                + " = " + localDateTime.toString();

        Cursor c = db.rawQuery(selectQuery, null);
        long daySetCode = 0;
        if (c != null && c.moveToFirst()) {
            daySetCode = c.getInt(c.getColumnIndex(ATTR_DAYS_WEEK));
            c.close();
        }

        db.close();

        return daySetCode;
    }

    public int getBadgeTimesCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + TABLE_BADGETIMES;

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
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

        db.close();

        return dailyMax;
    }

    public int getHoursPerDayCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + TABLE_HOURS_DAY;

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    // TODO delete this once safe to do so
    /*
    // SELECT DAYS_WEEK
    public boolean[] getDaysPerWeek(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_DAYS_WEEK + " WHERE " + ATTR_ID_DAYS_WEEK
                + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        int[] days = new int[7];
        days[0] = c.getInt(c.getColumnIndex(ATTR_MONDAY));
        days[1] = c.getInt(c.getColumnIndex(ATTR_TUESDAY));
        days[2] = c.getInt(c.getColumnIndex(ATTR_WEDNESDAY));
        days[3] = c.getInt(c.getColumnIndex(ATTR_THURSDAY));
        days[4] = c.getInt(c.getColumnIndex(ATTR_FRIDAY));
        days[5] = c.getInt(c.getColumnIndex(ATTR_SATURDAY));
        days[6] = c.getInt(c.getColumnIndex(ATTR_SUNDAY));

        boolean[] daySet = getIntToBool(days);

        c.close();
        db.close();

        return daySet;
    }

    public int getDaysPerWeekCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + TABLE_DAYS_WEEK;

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }
    */

    // --------------------------------------------------------------------------
    // UPDATE STATEMENTS

    // UPDATE BADGETIMES
    public int updateBadgeTimes(LocalDateTime localDateTimeOld, LocalDateTime localDateTimeNew) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ATTR_TIME, localDateTimeNew.toString());

        db.close();

        return db.update(TABLE_BADGETIMES, values, ATTR_TIME + " = ?",
                new String[] { String.valueOf(localDateTimeOld)});
    }

    // --------------------------------------------------------------------------
    // DELETE STATEMENTS

    // DELETE BADGETIMES
    public void deleteBadgeTimes(LocalDateTime localDateTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_BADGETIMES, ATTR_TIME +  " = ?",
                new String[] { String.valueOf(localDateTime)});
    }
}

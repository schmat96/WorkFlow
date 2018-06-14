package net.ict.workflow.workflow.model;

import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.util.TreeSet;

public class DatabaseHelperStub implements DatabaseHelperInterface {


    @Override
    public long insertBadgeTime(LocalDateTime localDateTime, float hours, long daysCode) {
        return 0;
    }

    @Override
    public long insertHoursPerDay(float hours) {
        return 0;
    }

    @Override
    public TreeSet<LocalDateTime> getAllBadgeTimes() {
        TreeSet<LocalDateTime> tree = new TreeSet<>();
        tree.add(LocalDateTime.of(2018,06,14,8,30));
        tree.add(LocalDateTime.of(2018,06,14,9,30));
        tree.add(LocalDateTime.of(2018,06,14,4,30));
        tree.add(LocalDateTime.of(2018,06,14,6,30));
        tree.add(LocalDateTime.of(2018,06,14,3,30));
        tree.add(LocalDateTime.of(2018,06,14,3,0));
        tree.add(LocalDateTime.of(2018,06,15,8,10));
        tree.add(LocalDateTime.of(2018,06,15,1,10));
        tree.add(LocalDateTime.of(2018,06,15,8,30));
        tree.add(LocalDateTime.of(2018,06,15,5,30));
        tree.add(LocalDateTime.of(2018,06,15,12,30));
        tree.add(LocalDateTime.of(2018,06,23,8,30));
        tree.add(LocalDateTime.of(2018,06,23,8,10));
        tree.add(LocalDateTime.of(2018,06,23,6,30));
        tree.add(LocalDateTime.of(2018,06,23,8,30));
        tree.add(LocalDateTime.of(2018,06,24,5,30));
        tree.add(LocalDateTime.of(2018,06,24,10,30));
        tree.add(LocalDateTime.of(2018,06,24,3,40));
        tree.add(LocalDateTime.of(2018,07,15,1,30));
        tree.add(LocalDateTime.of(2018,07,15,8,10));
        tree.add(LocalDateTime.of(2018,07,15,1,33));
        tree.add(LocalDateTime.of(2018,07,15,8,30));
        tree.add(LocalDateTime.of(2018,07,15,5,30));
        tree.add(LocalDateTime.of(2018,07,15,12,30));
        return tree;
    }

    @Override
    public float getBadgeTimeMax(LocalDateTime localDateTime) {
        return 0;
    }

    @Override
    public long getHoursPerDayId(float hours, SQLiteDatabase db) {
        return 0;
    }

    @Override
    public long getBadgeTimeDaySet(LocalDateTime localDateTime) {
        return 0;
    }

    @Override
    public int getBadgeTimesCount() {
        return 0;
    }

    @Override
    public float getHoursPerDay(long id) {
        return 0;
    }

    @Override
    public int getHoursPerDayCount() {
        return 0;
    }

    @Override
    public int updateBadgeTimes(LocalDateTime localDateTimeOld, LocalDateTime localDateTimeNew) {
        return 0;
    }

    @Override
    public void deleteBadgeTimes(LocalDateTime localDateTime) {

    }
}

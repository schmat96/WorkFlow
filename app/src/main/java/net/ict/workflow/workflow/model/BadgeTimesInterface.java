package net.ict.workflow.workflow.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 */

interface BadgeTimesInterface {


    public ArrayList<LocalDateTime> getTimeStampsInDate(LocalDate date);
    /**
     * @deprecated use {@link #getBadgedTime(CardType ct, LocalDateTime ldt)} instead.
     */
    @Deprecated
    public float getBadgedTimeDay(LocalDateTime date);

    /**
     * @deprecated use {@link #getBadgedTime(CardType ct, LocalDateTime ldt)} instead.
     */
    @Deprecated
    public float getBadgedTimeWeek(LocalDateTime date);

    /**
     * @deprecated use {@link #getBadgedTime(CardType ct, LocalDateTime ldt)} instead.
     */
    @Deprecated
    public float getBadgedTimeMonth(LocalDateTime date);

    public float getSecondsBetweenDays(long daysBetween, LocalDate startDate);

    public float getMax(CardType type, LocalDateTime[] ldt, LocalDateTime ldtSuperbe);

    long workingDays();

    int getDaysOfMonth(LocalDateTime ldt, Boolean[] daysToWork);
    public void addBadgeTime(LocalDateTime ldt, int daysCode);

    public void removeWithValue(LocalDateTime localDateTime);

    public void updateBadgeTime(LocalDateTime oldTime, LocalDateTime newTime);

    public float getBadgedTime(CardType ct, LocalDateTime ldt);
    public LocalDateTime[] lookForBadgedTime(LocalDateTime ldt, CardType ct);
}

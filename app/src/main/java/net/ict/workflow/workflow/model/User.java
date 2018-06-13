package net.ict.workflow.workflow.model;

import android.content.Context;
import android.util.Log;

import net.ict.workflow.workflow.Cards;
import net.ict.workflow.workflow.MainActivity;
import net.ict.workflow.workflow.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class User {
    private Integer id = 0;
    private static BadgeTimes badgeTimes;
    private LocalDateTime choosenDate;
    private ZoneOffset zoneOffSet;

    public User(Context context) {
        //#TODO checken ob der user schonmal eingeloggt war auf diesem Natel, wenn ja id = userID;
        if (User.badgeTimes == null) {
            User.badgeTimes = new BadgeTimes(context);
        }

        choosenDate = LocalDateTime.now();
        OffsetDateTime odt = OffsetDateTime.now ();
        zoneOffSet = odt.getOffset ();

    }

    public User(LocalDateTime date, Context context) {

        if (User.badgeTimes == null) {
            User.badgeTimes = new BadgeTimes(context);
        }

        choosenDate = date;
        OffsetDateTime odt = OffsetDateTime.now ();
        zoneOffSet = odt.getOffset ();

    }

    public void plusDate(CardType pos) {
        switch (pos) {
            case DAY:
                this.choosenDate = this.choosenDate.plusDays(1);
                break;
            case WEEK:
                this.choosenDate = this.choosenDate.plusWeeks(1);
                break;
            case MONTH:
                this.choosenDate = this.choosenDate.plusMonths(1);
                break;
            default:
                this.choosenDate = this.choosenDate.plusDays(1);
        }
    }

    public void minusDate(CardType pos) {
        switch (pos) {
            case DAY:
                this.choosenDate = this.choosenDate.minusDays(1);
                break;
            case WEEK:
                this.choosenDate = this.choosenDate.minusWeeks(1);
                break;
            case MONTH:
                this.choosenDate = this.choosenDate.minusMonths(1);
                break;
            default:
                this.choosenDate = this.choosenDate.minusDays(1);
        }

    }

    private String getDate(LocalDateTime date) {
        return date.getDayOfMonth()+";"+date.getMonthValue()+";"+date.getYear();
    }

    public LocalDateTime getChoosenDate() {
        return this.choosenDate;
    }

    public ZoneOffset getZoneOffSet() {
        return zoneOffSet;
    }



    public int getID() {
        return this.id;
    }

    /**
     * @deprecated use {@link #getBadgeTime(CardType ct, LocalDateTime ldt)} instead.
     */
    @Deprecated
    public static float getBadgedTimeDay(LocalDateTime ldt) {
        if (badgeTimes != null) {
            return badgeTimes.getBadgedTimeDay(ldt);
        }
        return 0f;
    }

    public static float getMaxTime(CardType ct, LocalDateTime ldt) {
        if (badgeTimes != null) {
            return badgeTimes.getMax(ct, ldt);
        }
        return 0f;
    }

    public static float getBadgeTime(CardType ct, LocalDateTime ldt) {
        if (badgeTimes != null) {
            return badgeTimes.getBadgedTime(ct, ldt);
        }
        return 0f;

    }

    public static void removeWithValue(LocalDateTime localDateTime) {
        if (badgeTimes != null) {
            badgeTimes.removeWithValue(localDateTime);
        }
    }

    public static ArrayList<LocalDateTime> getTimeStampsInDate(LocalDate ldt) {
        if (badgeTimes != null) {
            return badgeTimes.getTimeStampsInDate(ldt);
        }
        return null;
    }

    public static void addBadgeTime(LocalDateTime now) {
        if (badgeTimes != null) {
            badgeTimes.addBadgeTime(now);
        }
    }

    public static void updateBadgeTime(LocalDateTime oldTime, LocalDateTime newTime) {
        if (badgeTimes != null) {
            badgeTimes.updateBadgeTime(oldTime, newTime);

        }
    }
}

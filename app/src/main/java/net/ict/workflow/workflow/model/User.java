package net.ict.workflow.workflow.model;

import android.util.Log;

import net.ict.workflow.workflow.Cards;
import net.ict.workflow.workflow.MainActivity;
import net.ict.workflow.workflow.R;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class User {
    private Integer id = 0;
    private static BadgeTimes badgeTimes;
    private LocalDateTime choosenDate;
    private ZoneOffset zoneOffSet;

    public User() {
        //#TODO checken ob der user schonmal eingeloggt war auf diesem Natel, wenn ja id = userID;
        if (User.badgeTimes == null) {
            User.badgeTimes = new BadgeTimes();
            badgeTimes.init();
        }

        choosenDate = LocalDateTime.now();
        OffsetDateTime odt = OffsetDateTime.now ();
        zoneOffSet = odt.getOffset ();

    }

    public User(LocalDateTime date) {

        if (User.badgeTimes == null) {
            User.badgeTimes = new BadgeTimes();
            badgeTimes.init();
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

    public void addBadgeTime(LocalDateTime now) {
        badgeTimes.addBadgeTime(now);
    }

    public int getID() {
        return this.id;
    }

    public static BadgeTimes getBadgeTimes() {
        return User.badgeTimes;
    }


}

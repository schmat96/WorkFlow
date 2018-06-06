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
    private BadgeTimes badgeTimes;
    private Cards[] cards;
    private LocalDateTime choosenDate;
    private ZoneOffset zoneOffSet;

    public User() {
        //#TODO checken ob der user schonmal eingeloggt war auf diesem Natel, wenn ja id = userID;
        this.badgeTimes = new BadgeTimes();
        choosenDate = LocalDateTime.now();

        OffsetDateTime odt = OffsetDateTime.now ();
        zoneOffSet = odt.getOffset ();

        badgeTimes.init();
    }



    public int getID() {
        return this.id;
    }

    public BadgeTimes getBadgeTimes() {
        return this.badgeTimes;
    }


    public Cards[] getCards() {
        //TODO MainActivity Workaround finden um ma.getStrinf(R.string.Day) zu greiffen zu können.
        long timecurrent = System.currentTimeMillis();
        Log.e("User", "starting badgetimes");
        if (cards == null) {
            cards = new Cards[3];
            String datum = getDate(choosenDate);
            cards[0] = new Cards(8.24f, badgeTimes.getBadgedTimeDay(choosenDate), choosenDate, CardType.DAY);
            cards[1] = new Cards(54.24f, badgeTimes.getBadgedTimeWeek(choosenDate), choosenDate, CardType.WEEK);
            cards[2] = new Cards(200.05f, badgeTimes.getBadgedTimeMonth(choosenDate), choosenDate, CardType.MONTH);
        }
        Log.e("User", "finished loading"+(System.currentTimeMillis()-timecurrent));
        return cards;
    }

    public void reloadCard(int pos) {
        // TODO Position wird im Moment noch nicht bearbeitet. Die Idee ist das hier nur die Karte bearbeitet wird, welche mit der Pos reinkommt --> enums.
        // TODO MAX Wert muss hier auch noch programmatically gesetzt werden.
        //TODO MainActivity Workaround finden um ma.getStrinf(R.string.Day) zu greiffen zu können.
        long timecurrent = System.currentTimeMillis();
        Log.e("User", "starting badgetimes");
        String datum = getDate(choosenDate);
        cards[0].setZeit(badgeTimes.getBadgedTimeDay(choosenDate));
        cards[0].setDate(choosenDate);

        cards[1].setZeit(badgeTimes.getBadgedTimeWeek(choosenDate));
        cards[1].setDate(choosenDate);

        cards[2].setZeit(badgeTimes.getBadgedTimeMonth(choosenDate));
        cards[2].setDate(choosenDate);

        Log.e("User", "finished loading "+(System.currentTimeMillis()-timecurrent));
    }

    public void plusDate(int pos) {
        switch (pos) {
            case 0:
                this.choosenDate = this.choosenDate.plusDays(1);
                break;
            case 1:
                this.choosenDate = this.choosenDate.plusWeeks(1);
                break;
            case 2:
                this.choosenDate = this.choosenDate.plusMonths(1);
                break;
            default:
                this.choosenDate = this.choosenDate.plusDays(1);
        }
        this.reloadCard(pos);
    }

    public void minusDate(int pos) {
        switch (pos) {
            case 0:
                this.choosenDate = this.choosenDate.minusDays(1);
                break;
            case 1:
                this.choosenDate = this.choosenDate.minusWeeks(1);
                break;
            case 2:
                this.choosenDate = this.choosenDate.minusMonths(1);
                break;
            default:
                this.choosenDate = this.choosenDate.minusDays(1);
        }
        this.reloadCard(pos);
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
}

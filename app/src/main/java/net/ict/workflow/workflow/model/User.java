package net.ict.workflow.workflow.model;

import android.util.Log;

import net.ict.workflow.workflow.Cards;
import net.ict.workflow.workflow.MainActivity;
import net.ict.workflow.workflow.R;

import java.time.LocalDateTime;

public class User {
    private Integer id = 0;
    private BadgeTimes badgeTimes;
    private Cards[] cards;


    public User() {
        //#TODO checken ob der user schonmal eingeloggt war auf diesem Natel, wenn ja id = userID;
        this.badgeTimes = new BadgeTimes();
        badgeTimes.init();
    }

    public int getID() {
        return this.id;
    }

    public BadgeTimes getBadgeTimes() {
        return this.badgeTimes;
    }


    public Cards[] getCards(MainActivity ma, LocalDateTime date) {
        //TODO MainActivity Workaround finden um ma.getStrinf(R.string.Day) zu greiffen zu können.
        long timecurrent = System.currentTimeMillis();
        Log.e("User", "starting badgetimes");
        if (cards == null) {
            cards = new Cards[3];
            cards[0] = new Cards(8.24f, badgeTimes.getBadgedTimeDay(date), ma.getString(R.string.Day));
            cards[1] = new Cards(54.24f, badgeTimes.getBadgedTimeWeek(date), ma.getString(R.string.Week));
            cards[2] = new Cards(200.05f, badgeTimes.getBadgedTimeMonth(date), ma.getString(R.string.Month));
        }
        Log.e("User", "finished loading"+(System.currentTimeMillis()-timecurrent));
        return cards;
    }

    public void reloadCard(int pos, LocalDateTime date, MainActivity ma) {
        // TODO Position wird im Moment noch nicht bearbeitet. Die Idee ist das hier nur die Karte bearbeitet wird, welche mit der Pos reinkommt --> enums!
        // TODO MAX Wert muss hier auch noch programmatically gesetzt werden.
        //TODO MainActivity Workaround finden um ma.getStrinf(R.string.Day) zu greiffen zu können.
        long timecurrent = System.currentTimeMillis();
        Log.e("User", "starting badgetimes");
        cards[0] = new Cards(8.24f, badgeTimes.getBadgedTimeDay(date), ma.getString(R.string.Day));
        cards[1] = new Cards(54.24f, badgeTimes.getBadgedTimeWeek(date), ma.getString(R.string.Week));
        cards[2] = new Cards(200.05f, badgeTimes.getBadgedTimeMonth(date), ma.getString(R.string.Month));
        Log.e("User", "finished loading "+(System.currentTimeMillis()-timecurrent));

    }

    public void addBadgeTime(LocalDateTime now) {
        badgeTimes.addBadgeTime(now);
    }
}

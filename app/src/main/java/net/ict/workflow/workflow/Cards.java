package net.ict.workflow.workflow;

import android.util.Log;

import net.ict.workflow.workflow.model.CardType;
import net.ict.workflow.workflow.model.User;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Cards {

    private User user;
    private CardType cardType;

    public Cards(User user, CardType cardType) {
        this.user = user;
        this.cardType = cardType;
    }



    public String title() {
        String returnState = "";
        return this.user.getChoosenDate().toString();
        /*
        switch (cardType) {

            case DAY:
                returnState = date.getDayOfWeek().toString();
                break;
            case WEEK:
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
                returnState = "Week " + weekNumber;
                break;
            case MONTH:
                returnState = date.getMonth().toString();
                break;
        }
        return returnState;
        */
    }

    public String getDate() {
        String returnState = "";
        switch (cardType) {
            case DAY:
                returnState = this.user.getChoosenDate().getDayOfWeek().toString();
                break;
            case WEEK:
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int weekNumber = this.user.getChoosenDate().get(weekFields.weekOfWeekBasedYear());
                returnState = "Week " + weekNumber;
                break;
            case MONTH:
                returnState = this.user.getChoosenDate().getMonth().toString();
                break;
        }
        return returnState;
    }



    public float getMax() {
        float ret = 0f;
        switch (this.cardType) {
            case DAY:
                ret = 8f;
                break;
            case WEEK:
                ret = 54f;
                break;
            case MONTH:
                ret =  200f;
                break;
        }
        return ret;
    }

    public float getZeit() {
        float ret = 0f;
        switch (this.cardType) {
            case DAY:
                ret = this.user.getBadgeTimes().getBadgedTimeDay(this.user.getChoosenDate());
                break;
            case WEEK:
                ret = this.user.getBadgeTimes().getBadgedTimeWeek(this.user.getChoosenDate());
                break;
            case MONTH:
                ret =  this.user.getBadgeTimes().getBadgedTimeMonth(this.user.getChoosenDate());
                break;
        }
        return ret;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }
}

package net.ict.workflow.workflow;

import android.util.Log;

import net.ict.workflow.workflow.model.CardType;
import net.ict.workflow.workflow.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        //return this.user.getChoosenDate().toString();
        DateTimeFormatter formatter;
        formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        return this.user.getChoosenDate().format(formatter);


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
        return User.getMaxTime(this.cardType, this.user.getChoosenDate());
    }

    public float getZeit() {
        float zeit = 0f;
        switch (cardType) {

            case DAY:
                zeit = User.getBadgedTimeDay(this.user.getChoosenDate());
                break;
            case WEEK:
                zeit = User.getBadgedTimeWeek(this.user.getChoosenDate());
                break;
            case MONTH:
                zeit = User.getBadgedTimeMonth(this.user.getChoosenDate());
                break;
        }
        return zeit;

    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }

}

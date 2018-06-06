package net.ict.workflow.workflow;

import net.ict.workflow.workflow.model.CardType;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Cards {
    private float max = 8.24f;
    private float zeit = 4.2f;
    private LocalDateTime date;
    private CardType cardType;

    public Cards(float max, float zeit, LocalDateTime date, CardType cardType) {
        this.max = max;
        this.zeit = zeit;
        this.date = date;
        this.cardType = cardType;
    }


    public float getMax() {
        return max;
    }

    public float getZeit() {
        return zeit;
    }

    public String title() {
        String returnState = "";
        return this.date.toString();
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
    }


    public void setZeit(float zeit) {
        this.zeit = zeit;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }
}

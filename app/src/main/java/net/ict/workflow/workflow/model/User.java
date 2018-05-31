package net.ict.workflow.workflow.model;

import net.ict.workflow.workflow.Cards;

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


    public Cards[] getCards() {
        if (cards == null) {
            cards = new Cards[3];
            cards[0] = new Cards(8.24f, 5.02f, "Tag");
            cards[1] = new Cards(54.24f, 29.02f, "Woche");
            cards[2] = new Cards(200.05f, 266.05f, "Monat");
        }
        return cards;
    }
}

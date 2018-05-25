package net.ict.workflow.workflow.model;

public class User {
    private Integer id = 0;
    private BadgeTimes badgeTimes;

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
}

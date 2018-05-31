package net.ict.workflow.workflow;

public class Cards {
    private float max = 8.24f;
    private float zeit = 4.2f;
    private String title;

    public Cards(float max, float zeit, String title) {
        this.max = max;
        this.zeit = zeit;
        this.title = title;
    }

    public float getMax() {
        return max;
    }

    public float getZeit() {
        return zeit;
    }

    public String title() {
        return title;
    }
}

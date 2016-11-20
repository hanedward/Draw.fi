package csci201.han.edward.fi.draw;

/**
 * Created by tanujamohan on 11/20/16.
 */
public class Message {
    private String name;
    private boolean isDone;
    private int points;
    private int order;

    public Message(String name, boolean isDone, int points, int order) {
        this.name = name;
        this.isDone = isDone;
        this.points = points;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public boolean isDone() {
        return isDone;
    }

    public int getPoints() {
        return points;
    }

    public int getOrder() {
        return order;
    }


}

package csci201.han.edward.fi.draw;

import java.util.Random;

/**
 * Created by AlecFong on 11/20/16.
 */

public class PointGenerator {

    private int pointVal = 0;

    public PointGenerator() {}

    public int getPoint() {
        Random rGen = new Random();
        pointVal = rGen.nextInt(10)+2;
        return pointVal;
    }
}

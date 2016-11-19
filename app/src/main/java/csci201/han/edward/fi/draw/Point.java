package csci201.han.edward.fi.draw;

import android.graphics.Canvas;
import android.graphics.Paint;

/*
a Point is the most basic thing that the user can put on the screen.
* Points will get drawn when the user touches the screen.
* PointConnection inherits from this class.
* */

public class Point {
    public float x, y;
    public int col;
    public int width;

    public Point(){
        //default constructor
    }

    public Point(final float x, final float y, final int col, final int width) {
        this.x = x;
        this.y = y;
        this.col = col;
        this.width = width;
    }

    public void draw(final Canvas canvas, final Paint paint) {
        paint.setColor(col);
        canvas.drawCircle(x, y, width/2, paint); //for handlings curves
    }

}

package csci201.han.edward.fi.draw;

import android.graphics.Canvas;
import android.graphics.Paint;

/*
* PointConnections are essentially Points between Points.
* This class is to fill in the points on the touchscreen so that the drawing doesn't look like MSPaint spraypaint.
* Without this class, the user would be drawing individual, unconnected points rather than lines.
* */

public class PointConnection extends Point {

    private final Point neighbor;

    public PointConnection(final float x, final float y, final int color, final Point neighbor, final int width) {
        super(x, y, color, width);
        this.neighbor = neighbor;
    }

    @Override
    public void draw(final Canvas canvas, final Paint paint) {
        paint.setColor(color);
        paint.setStrokeWidth(width);
        canvas.drawLine(x, y, neighbor.x, neighbor.y, paint);
        canvas.drawCircle(x, y, width/2, paint); //adds more vertices between curved line segments
    }
}

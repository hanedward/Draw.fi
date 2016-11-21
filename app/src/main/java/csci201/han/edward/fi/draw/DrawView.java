package csci201.han.edward.fi.draw;


import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {

    //the points to be drawn on the screen (need different array for each of 12 colors)


    List<Point> allPoints = new ArrayList<Point>(); //for checking if a point already exists

    public int orangeColor;
    public int pinkColor;
    public int purpleColor;
    public int brownColor;
    public int darkGreenColor;

    private Paint paint = new Paint();
    private int paintColor;
    private int width;

    public DrawView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    //our DrawView constructor
    public DrawView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paintColor = Color.BLACK; //set default color
        width = 15; //set default width
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        paint.setAntiAlias(true);

        //set custom colors
        orangeColor = Color.parseColor("#FF8800");
        darkGreenColor = Color.parseColor("#028934");
        brownColor = Color.parseColor("#FF8800");
        purpleColor = Color.parseColor("#6d351b");
        pinkColor = Color.parseColor("#ffa8d2");
    }


    //forces points to be drawn NOW rather than during next action
    public void forceRedraw() {
        invalidate();
    }

    public void changeBrushSize(int size){
        width = size;
    }

    public void clearCanvas(){
        allPoints.clear();
    }


    @Override
    public void onDraw(Canvas canvas) {
        for (Point point : allPoints) point.draw(canvas, paint);
    }

    public void changeColor(int c){
        paintColor = c;
    }

    public boolean onTouch(View view, MotionEvent event) {

        Point point;

        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            //Any time you draw on the screen, make PointConnections to fill in between your Points.
            //If you make this a normal Point rather than a PointConnection, unconnected dots appear, not lines :(
            point = new PointConnection(event.getX(), event.getY(), paintColor, allPoints.get(allPoints.size() - 1), width);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            point = new Point(event.getX(), event.getY(), paintColor, width);
        } else {
            return false;
        }

        //TO PREVENT OVERLAP PROBLEMS/handle colors being drawn-over:
        for (int i = 0; i < allPoints.size(); i++){
            if (allPoints.get(i).isSamePoint(point)) { //if point exists (that spot was already drawn on)
                allPoints.get(i).color = paintColor;
            }
        }
        //add point to our collection of all drawn points
        allPoints.add(point);

        forceRedraw();
        return true;
    }
}
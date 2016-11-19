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

    List<Point> redPoints = new ArrayList<Point>();
    List<Point> orangePoints = new ArrayList<Point>();
    List<Point> yellowPoints = new ArrayList<Point>();
    List<Point> greenPoints = new ArrayList<Point>();
    List<Point> cyanPoints = new ArrayList<Point>();
    List<Point> bluePoints = new ArrayList<Point>();
    List<Point> magentaPoints = new ArrayList<Point>();

    List<Point> whitePoints = new ArrayList<Point>(); //for erasing
    List<Point> blackPoints = new ArrayList<Point>();
    List<Point> grayPoints = new ArrayList<Point>();
    List<Point> darkGreenPoints = new ArrayList<Point>();
    List<Point> brownPoints = new ArrayList<Point>();
    List<Point> pinkPoints = new ArrayList<Point>();
    List<Point> purplePoints = new ArrayList<Point>();

    List<Point> allPoints = new ArrayList<Point>(); //for checking if a point already exists
    List<PointPair> allPointPairs = new ArrayList<PointPair>(); //to find which color array an exist point lives in

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

    @Override
    public void onDraw(Canvas canvas) {

        // for each color point array + eraser, draw on canvas (14 for loops)
        for (Point point : redPoints)   point.draw(canvas, paint);
        for (Point point : orangePoints) point.draw(canvas, paint);
        for (Point point : yellowPoints) point.draw(canvas, paint);
        for (Point point : greenPoints) point.draw(canvas, paint);
        for (Point point : cyanPoints)  point.draw(canvas, paint);
        for (Point point : bluePoints)  point.draw(canvas, paint);
        for (Point point : magentaPoints)  point.draw(canvas, paint);
        for (Point point : whitePoints) point.draw(canvas, paint);
        for (Point point : blackPoints) point.draw(canvas, paint);
        for (Point point : grayPoints) point.draw(canvas, paint);
        for (Point point : darkGreenPoints) point.draw(canvas, paint);
        for (Point point : brownPoints) point.draw(canvas, paint);
        for (Point point : pinkPoints) point.draw(canvas, paint);
        for (Point point : purplePoints) point.draw(canvas, paint);
    }

    public boolean onTouch(View view, MotionEvent event) {

        Point point;
        List<Point> colorArray;

        if(paintColor == Color.RED) colorArray = redPoints;
        else if (paintColor == orangeColor) colorArray = orangePoints;
        else if (paintColor == Color.YELLOW)  colorArray = yellowPoints;
        else if (paintColor == Color.GREEN)  colorArray = greenPoints;
        else if (paintColor == Color.CYAN)  colorArray = cyanPoints;
        else if (paintColor == Color.BLUE)  colorArray = bluePoints;
        else if (paintColor == Color.MAGENTA)  colorArray = magentaPoints;

        else if (paintColor == Color.BLACK)  colorArray = blackPoints;
        else if (paintColor == Color.LTGRAY)  colorArray = grayPoints;
        else if (paintColor == darkGreenColor)  colorArray = darkGreenPoints;
        else if (paintColor == brownColor)  colorArray = brownPoints;
        else if (paintColor == pinkColor)  colorArray = pinkPoints;
        else if (paintColor == purpleColor)  colorArray = purplePoints;
        else colorArray = whitePoints; //erasing


        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            //Any time you draw on the screen, make PointConnections to fill in between your Points.
            //If you make this a normal Point rather than a PointConnection, unconnected dots appear, not lines :(
            point = new PointConnection(event.getX(), event.getY(), paintColor, colorArray.get(colorArray.size() - 1), width);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            point = new Point(event.getX(), event.getY(), paintColor, width);
        } else {
            return false;
        }

        //TO PREVENT OVERLAP PROBLEMS/handle colors being drawn-over:
        for (int i = 0; i < allPoints.size(); i++){
            if (allPoints.get(i).equals(point)) { //if point exists (that spot was already drawn on)
                List<Point> oldColorArray = allPointPairs.get(i).getColorArray(); // get which colorArray it was in
                oldColorArray.remove(i); //remove it from that color array
                allPoints.remove(i); //remove it from allPoints array
                allPointPairs.remove(i); //remove it from allPointPairs array
            }
        }
        //add point to its current color array and both allPoints arrays
        colorArray.add(point);
        allPoints.add(point);
        PointPair pp = new PointPair(point, paintColor);
        allPointPairs.add(pp);

        forceRedraw();
        return true;
    }

    public void changeColor(int c){
        paintColor = c;
    }

    public class PointPair extends Point{

        Point mPoint; //the point itself
        int mPointColor; // so we can later find which array the point is stored in

        public PointPair(Point p, int pi){
            mPoint = p;
            mPointColor = pi;
        }

        public List<Point> getColorArray(){
            List<Point> pColorArray = null;

            if(mPointColor == Color.BLACK)
                pColorArray = blackPoints;
            else if (mPointColor == Color.LTGRAY)
                pColorArray = grayPoints;
            else if (mPointColor == Color.WHITE)
                pColorArray = whitePoints;
            else if (mPointColor == brownColor)
                pColorArray = brownPoints;
            else if (mPointColor == Color.GREEN)
                pColorArray = greenPoints;
            else if (mPointColor == darkGreenColor)
                pColorArray = darkGreenPoints;
            else if (mPointColor == Color.CYAN)
                pColorArray = cyanPoints;
            else if (mPointColor == Color.BLUE)
                pColorArray = bluePoints;
            else if (mPointColor == pinkColor)
                pColorArray = pinkPoints;
            else if (mPointColor == purpleColor)
                pColorArray = purplePoints;
            else if (mPointColor == orangeColor)
                pColorArray = orangePoints;
            else if (mPointColor == Color.YELLOW)
                pColorArray = yellowPoints;
            else if (mPointColor == Color.MAGENTA)
                pColorArray = magentaPoints;

            return pColorArray;
        }

        public Point getPoint(){
            return mPoint;
        }
    }

}
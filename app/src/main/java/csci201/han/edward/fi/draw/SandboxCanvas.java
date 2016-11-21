package csci201.han.edward.fi.draw;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The only files you need to run the barebones canvas:
 * - GameCanvas.java
 * - Point.java.java
 * - PointConnection.java
 * - DrawView.java
 *
 * No widets in activity_main.xml are needed if you create the DrawView dynamically and make it take up the whole screen
 *
 */

public class SandboxCanvas extends AppCompatActivity {

    private DrawView canvas;
    private Toolbar mToolbar;
    private Toolbar mToolbar1;
    private TextView keyword, timerTextView, clearButton;
    private ImageView smallBrush, mediumBrush, largeBrush;

    public int orangeColor;
    public int purpleColor;
    public int darkGreenColor;
    public int pinkColor;
    public int brownColor;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        myToolbar.setBackgroundColor(Color.parseColor("#FF3b5998"));

        //set custom colors
        orangeColor = ContextCompat.getColor(getApplicationContext(), R.color.orange);
        purpleColor = ContextCompat.getColor(getApplicationContext(), R.color.purple);
        pinkColor = ContextCompat.getColor(getApplicationContext(), R.color.pink);
        darkGreenColor = ContextCompat.getColor(getApplicationContext(), R.color.darkGreen);
        brownColor = ContextCompat.getColor(getApplicationContext(), R.color.brown);


        clearButton = (TextView) findViewById(R.id.clearButton);
        smallBrush = (ImageView) findViewById(R.id.smallSize);
        mediumBrush = (ImageView) findViewById(R.id.mediumSize);
        largeBrush = (ImageView) findViewById(R.id.largeSize);
        canvas = (DrawView) findViewById(R.id.canvas); //get the drawview
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar1 = (Toolbar) findViewById(R.id.main_toolbar1); //get second toolbar

        mToolbar.inflateMenu(R.menu.menu_toolbar_colors1);//inflate first toolbar
        mToolbar1.inflateMenu(R.menu.menu_toolbar_colors2);//inflate second toolbar

        //CHANGE ALL THE ICON COLORS
        for (int i = 0; i < mToolbar.getMenu().size(); i++) {
            MenuItem pencilIcon = mToolbar.getMenu().getItem(i); //find the MenuItem
            Drawable drawable = pencilIcon.getIcon(); //create a drawable from the icon
            if (drawable != null) {
                // If we don't mutate the drawable, then all drawables with this id will have a color filter applied to it
                drawable.mutate();
                //find which color the icon should be
                int iconColor;
                if(pencilIcon.getItemId() == R.id.menu_RED)
                    iconColor = Color.RED;
                else if (pencilIcon.getItemId() == R.id.menu_ORANGE)
                    iconColor = orangeColor;
                else if (pencilIcon.getItemId() == R.id.menu_YELLOW)
                    iconColor = Color.YELLOW;
                else if (pencilIcon.getItemId() == R.id.menu_GREEN)
                    iconColor = Color.GREEN;
                else if (pencilIcon.getItemId() == R.id.menu_CYAN)
                    iconColor = Color.CYAN;
                else if (pencilIcon.getItemId() == R.id.menu_BLUE)
                    iconColor = Color.BLUE;
                else
                    iconColor = Color.BLACK; //for eraser icon and black color

                drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
                drawable.setAlpha(255);
            }
        }

        //set icon colors for second toolbar
        for (int i = 0; i < mToolbar1.getMenu().size(); i++) {
            MenuItem pencilIcon = mToolbar1.getMenu().getItem(i); //find the MenuItem
            Drawable drawable = pencilIcon.getIcon(); //create a drawable from the icon
            if (drawable != null) {
                // If we don't mutate the drawable, then all drawables with this id will have a color filter applied to it
                drawable.mutate();
                //find which color the icon should be
                int iconColor = Color.BLACK;

                if (pencilIcon.getItemId() == R.id.menu_RED)
                    iconColor = Color.RED;
                else if (pencilIcon.getItemId() == R.id.menu_BROWN)
                    iconColor = brownColor;
                else if (pencilIcon.getItemId() == R.id.menu_DARKGREEN)
                    iconColor = darkGreenColor;
                else if (pencilIcon.getItemId() == R.id.menu_MAGENTA)
                    iconColor = Color.MAGENTA;
                else if (pencilIcon.getItemId() == R.id.menu_PURPLE)
                    iconColor = purpleColor;
                else if (pencilIcon.getItemId() == R.id.menu_ERASER)
                    iconColor= pinkColor;

                drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
                drawable.setAlpha(255);
            }
        }


        canvas.setBackgroundColor(Color.WHITE); //set default background to be white
        canvas.requestFocus();

        mToolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //second menu items
                    case R.id.menu_ERASER:
                        canvas.changeColor(Color.WHITE); //change color
                        break;
                    case R.id.menu_BLACK:
                        canvas.changeColor(Color.BLACK); //change color
                        break;
                    case R.id.menu_DARKGREEN:
                        canvas.changeColor(darkGreenColor); //change color
                        break;
                    case R.id.menu_BROWN:
                        canvas.changeColor(brownColor);
                        break;
                    case R.id.menu_PURPLE:
                        canvas.changeColor(purpleColor);
                        break;
                    case R.id.menu_MAGENTA:
                        canvas.changeColor(Color.MAGENTA);
                        break;
                }
                return false;
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //see what the id of the menu item is
                switch (item.getItemId()){
                    case R.id.menu_RED:
                        canvas.changeColor(Color.RED); //change color
                        break; //JUST break, don't finish()!! Ends app!
                    case R.id.menu_ORANGE:
                        canvas.changeColor(orangeColor);
                        break;
                    case R.id.menu_YELLOW:
                        canvas.changeColor(Color.YELLOW);
                        break;
                    case R.id.menu_GREEN:
                        canvas.changeColor(Color.GREEN);
                        break;
                    case R.id.menu_CYAN:
                        canvas.changeColor(Color.CYAN);
                        break;
                    case R.id.menu_BLUE:
                        canvas.changeColor(Color.BLUE);
                        break;
                    case R.id.home:
                        System.out.println("Clicked the back button");
                        Intent login = new Intent(SandboxCanvas.this, LoginGUI.class);
                        startActivity(login);
                        break;

                }
                return false;
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.clearCanvas();
            }
        });
        smallBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.changeBrushSize(15);
            }
        });
        mediumBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.changeBrushSize(30);
            }

        });
        largeBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.changeBrushSize(50);
            }
        });
    } //end onCreate()

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("Clicked the back button");
                Intent login = new Intent(SandboxCanvas.this, LoginGUI.class);
                startActivity(login);
                break;
            default: break;
        }
        return true;
    }



}


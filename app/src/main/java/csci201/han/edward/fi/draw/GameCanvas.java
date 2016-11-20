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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//MAIN ACTIVITY FOR THE CANVAS
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

public class GameCanvas extends AppCompatActivity {

    private DrawView canvas;
    private Toolbar mToolbar;
    private Toolbar mToolbar1;
    private TextView keyword;
    private TextView timerTextView;

    public int orangeColor;
    public int purpleColor;
    public int darkGreenColor;
    public int pinkColor;
    public int brownColor;

    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    String playerOpponent;
    String playerMe;

    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            System.out.println("entered");

            long millis = System.currentTimeMillis() - startTime;
            System.out.println(millis);
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            seconds = 30 - seconds;


            timerTextView.setText(String.format("%d:%02d", minutes, seconds));


            if (seconds <= 0) {
                timerTextView.setText("Time is up!");
            }

            timerHandler.postDelayed(this, 500);

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

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


        Bundle inBundle = getIntent().getExtras();
        playerOpponent = inBundle.get("player1").toString();
        playerMe = inBundle.get("player2").toString();
        timerTextView = (TextView) findViewById(R.id.timerLabel);
        keyword = (TextView) findViewById(R.id.keywordLabel);

        mReference.child("users").child(playerMe).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                keyword.setText(dataSnapshot.child("keyword").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        canvas = (DrawView) findViewById(R.id.canvas); //get the drawview
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar); //get first toolbar
        mToolbar1 = (Toolbar) findViewById(R.id.main_toolbar1); //get second toolbar

        mToolbar.inflateMenu(R.menu.menu_toolbar_colors1);//inflate first  toolbar
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
        } //end gigant0r for-loop

        //set icon colors for second toolbar
        for (int i = 0; i < mToolbar1.getMenu().size(); i++) {
            MenuItem pencilIcon = mToolbar1.getMenu().getItem(i); //find the MenuItem
            Drawable drawable = pencilIcon.getIcon(); //create a drawable from the icon
            if (drawable != null) {
                // If we don't mutate the drawable, then all drawables with this id will have a color filter applied to it
                drawable.mutate();
                //find which color the icon should be
                int iconColor = Color.WHITE;
                int flag = 0; //for eraser icon

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
                    flag = 1;
                else
                    iconColor = Color.BLACK; //for black pencil

                if (flag < 1) drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
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

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
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

                }
                return false;
            }
        });
    } //end onCreate()

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mReference.child("users").child(playerMe).child("match").setValue("false");
                Intent login = new Intent(GameCanvas.this, LoginGUI.class);
                finish();
                startActivity(login);
                break;
            default: break;
        }
        return true;
    }

}

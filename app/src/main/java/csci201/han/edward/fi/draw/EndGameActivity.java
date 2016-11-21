package csci201.han.edward.fi.draw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class EndGameActivity extends AppCompatActivity {

    private String myScore;
    private String opponentScore;
    private DatabaseReference mReference;
    private String myName;
    private String opponentName;
    private TextView myPoints;
    private TextView theirPoints;
    private TextView myStash;
    private TextView myUsername;
    private TextView theirUsername;
    private TextView win;
    public FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        myPoints = (TextView) findViewById(R.id.yourScore);
        theirPoints = (TextView) findViewById(R.id.opponentScore);
        myStash = (TextView) findViewById(R.id.currentStash);
        win = (TextView) findViewById(R.id.resultLabel);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        Bundle inBundle = getIntent().getExtras();
        myName = inBundle.get("me").toString();
        opponentName = inBundle.get("them").toString();

        mReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myScore = (Long.toString((long) dataSnapshot.child(myName).child("score").getValue()));
                opponentScore = (Long.toString((long) dataSnapshot.child(opponentName).child("score").getValue()));
                if (Integer.parseInt(myScore) == Integer.parseInt(opponentScore)) {
                    win.setText("It's a tie!");
                } else if (Integer.parseInt(myScore) > Integer.parseInt(opponentScore)) {
                    win.setText("You Won!");
                } else {
                    win.setText("You Lost");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myPoints.setText(myScore);
        theirPoints.setText(opponentScore);


    }

}
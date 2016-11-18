package csci201.han.edward.fi.draw;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KeywordActivity extends AppCompatActivity {

    private TextView mKeywordView;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    String playerOpponent;
    String playerMe;
    String mKeyword;

    String originalKey;
    String uid;
    String fName;
    String lName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        myToolbar.setBackgroundColor(Color.parseColor("#FF3b5998"));

        Bundle inBundle = getIntent().getExtras();
        playerOpponent = inBundle.get("player1").toString();
        playerMe = inBundle.get("player2").toString();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mKeywordView = (TextView) findViewById(R.id.keword_holder);


        mReference.child("users").child(playerMe).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mKeyword = (String)dataSnapshot.child("keyword").getValue();
                mKeywordView.setText(mKeyword);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: logout(); break;
            default: break;
        }
        return true;
    }

    public void logout() {

        mReference.child("users").child(playerMe).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                originalKey = dataSnapshot.child("key").getValue().toString();
                uid = dataSnapshot.child("uid").getValue().toString();
                fName = dataSnapshot.child("firstName").getValue().toString();
                lName = dataSnapshot.child("lastName").getValue().toString();



                Player toAdd = new Player(uid, fName, lName, originalKey);
                toAdd.setMatched("false");
                mReference.child("lobby_users").child(originalKey).setValue(toAdd);

               playerOpponent = dataSnapshot.child("opponentKey").getValue().toString();
                System.out.println("Player opponent should be a key: " + playerOpponent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mReference.child("users").child(playerMe).child("opponentKey").setValue("none");
        mReference.child("users").child(playerMe).child("keyword").setValue("");
        mReference.child("users").child(playerMe).child("match").setValue("false");
//        mReference.child("users").child(playerOpponent).child("opponentKey").setValue("none");
//        mReference.child("users").child(playerOpponent).child("keyword").setValue("");
//        mReference.child("users").child(playerOpponent).child("match").setValue("false");


//        Intent lobby = new Intent(KeywordActivity.this, LobbyActivity.class);
//        lobby.putExtra("name", fName);
//        lobby.putExtra("surname", lName);
//        lobby.putExtra("id", uid);
//        lobby.putExtra("key", uid);
//        startActivity(lobby);
        finish();
        //LoginManager.getInstance().logOut();
    }
}

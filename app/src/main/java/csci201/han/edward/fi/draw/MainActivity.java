package csci201.han.edward.fi.draw;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public Button logoutButton;
    public TextView welcomePrompt;
    public TextView idPrompt;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    int pair;
    String id;
    String key;
    boolean isSecondUser = false;
    String player1 = null;
    String player2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        logoutButton = (Button) findViewById(R.id.logout_button);
        welcomePrompt = (TextView) findViewById(R.id.welcome_prompt);
        idPrompt = (TextView) findViewById(R.id.id_prompt);

        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        id = inBundle.get("id").toString();

        welcomePrompt.setText("Welcome " + name + " " + surname);
        idPrompt.setText("Your id is " + id);

        mReference.child("lobby_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numChildren = (int)dataSnapshot.getChildrenCount();
                if(numChildren != 0 && numChildren != 1) {
                    isSecondUser = true;
                }

                if(isSecondUser) {
                    int counter = 0;
                    for(DataSnapshot c : dataSnapshot.getChildren()) {
                        if(counter == 0) {
                            String value = (String)c.child("uid").getValue();
                            player1 = value;
                            player2 = id;
                            String key = (String) c.getKey();
                            mReference.child("users").child(value).child("opponentKey").setValue(id);
                            mReference.child("users").child(id).child("opponentKey").setValue(value);
                            mReference.child("lobby_users").child(key).removeValue();
                        }
                        if (counter == 1) {
                            mReference.child("lobby_users").child(c.getKey()).removeValue();
                        }
                    }
                    setKeyWord(player1, player2);
                    nextActivity();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                mReference.child("lobby_users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot c : dataSnapshot.getChildren()) {
                            if(c.child("uid").getValue().equals(id)) {
                                mReference.child("lobby_users").child(c.getKey()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent login = new Intent(MainActivity.this, LoginGUI.class);
                startActivity(login);
                finish();
            }
        });

    }

    private void setKeyWord(final String playerId, final String opponentId) {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int keywordIndex = (int) (Math.random() * dataSnapshot.child("keywords").getChildrenCount());
                String word = (String) dataSnapshot.child("keywords").child("" + keywordIndex).getValue();
                mReference.child("users").child(playerId).child("keyword").setValue(word);
                mReference.child("users").child(opponentId).child("keyword").setValue(word);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void nextActivity() {
        Intent keywordIntent = new Intent(MainActivity.this, KeywordActivity.class);
        keywordIntent.putExtra("player1", player1);
        keywordIntent.putExtra("player2", player2);
        startActivity(keywordIntent);
    }
}

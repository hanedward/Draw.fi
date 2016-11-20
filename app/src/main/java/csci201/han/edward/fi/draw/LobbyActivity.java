package csci201.han.edward.fi.draw;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class LobbyActivity extends AppCompatActivity {


    public Button logoutButton;
    public TextView welcomePrompt;
    public TextView searchingPrompt;
    public TextView idPrompt;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    public Button retryButton;
    public Button noButton;
    public TextView opponentDialoguePrompt;
    String id;
    String key;
    String mKey;
    String name;
    String surname;
    boolean isSecondUser = false;
    String player1 = null;
    String player2 = null;
    String opponentName;
    private Dialog settingGameDialog;
    private int counter = 0;
    View settingView;

    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        FacebookSdk.sdkInitialize(getApplicationContext());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        myToolbar.setBackgroundColor(Color.parseColor("#FF3b5998"));

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();

        counter = 0;
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        logoutButton = (Button) findViewById(R.id.logout_button);
        welcomePrompt = (TextView) findViewById(R.id.welcome_prompt);
        searchingPrompt = (TextView) findViewById(R.id.searching_prompt);
        retryButton = (Button) findViewById(R.id.retryButton);
        noButton = (Button) findViewById(R.id.noButton);

        LayoutInflater v = LayoutInflater.from(this);
        settingView = v.inflate(R.layout.setting_game_layout, null);

        Bundle inBundle = getIntent().getExtras();
        name = (String)inBundle.get("name");
        surname = (String)inBundle.get("surname");
        id = (String)inBundle.get("id");
        mKey = inBundle.getString("key");

        String prompt = "Welcome " + name;
        welcomePrompt.setText(prompt);

        mReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!isSecondUser) {
                    for(DataSnapshot c : dataSnapshot.getChildren()) {
                        if(c.getKey().equals(id)) {
                            if(c.child("match").getValue().equals("true")) {
                                String opponentKey = (String)c.child("opponentKey").getValue();
                                player2 = id;
                                player1 = opponentKey;
                                nextActivity(opponentKey);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mReference.child("lobby_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numChildren = (int)dataSnapshot.getChildrenCount();
                if(numChildren != 0 && numChildren != 1) {
                    mReference.child("users").child(name).child("second").setValue(true);
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
                        counter++;
                    }
                    mReference.child("users").child(player1).child("match").setValue("true");
                    mReference.child("users").child(player2).child("match").setValue("true");
                    setKeyWord(player1, player2);
                    nextActivity(player1);
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
                        String opponentKey = null;
                        for(DataSnapshot c : dataSnapshot.getChildren()) {
                            if(c.child("uid").getValue().equals(id)) {
                                //NEED TO CLEAR MY OPPONENT AND MY OPPONENT OPPONENT'S IN USER TREE
                                opponentKey = c.child("opponentKey").getValue().toString();
                                mReference.child("lobby_users").child(c.getKey()).removeValue();
                            }
                        }
                        for(DataSnapshot c : dataSnapshot.getChildren()) {
                            if(c.child("uid").getValue().equals(opponentKey)) {
                                mKey = c.getKey();
                                mReference.child("lobby_users").child(c.getKey()).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent login = new Intent(LobbyActivity.this, LoginGUI.class);
                startActivity(login);
                finish();
            }
        });

        createTimer();
        createTimer();

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

    private void nextActivity(String oppKey) {
        player1 = oppKey;
        settingGameDialog = new Dialog(this);
        settingGameDialog.setContentView(R.layout.setting_game_layout);
        settingGameDialog.setCancelable(false);

        if(!this.isFinishing()) {
            settingGameDialog.show();

            final Timer timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    settingGameDialog.dismiss();
                    Intent gameIntent = new Intent(LobbyActivity.this, GameCanvas.class);
                    System.out.println("Player 1 value: " + player1);
                    System.out.println("Player 2 value: " + player2);
                    gameIntent.putExtra("player1", player1);
                    gameIntent.putExtra("player2", player2);
                    startActivity(gameIntent);
                }
            }, 4000);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: logoutButton.performClick(); break;
            default: break;
        }
        return true;
    }

    public void createTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mReference.child("lobby_users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot c : dataSnapshot.getChildren()) {
                                    if(c.child("uid").getValue().equals(id)) {
                                        mKey = c.getKey();
                                        mReference.child("lobby_users").child(c.getKey()).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        searchingPrompt.setText("Unable to find an opponent...");
                        avi.setVisibility(View.GONE);
                        retryButton.setVisibility(View.VISIBLE);
                        retryButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                avi.setVisibility(View.VISIBLE);
                                retryButton.setVisibility(View.GONE);
                                noButton.setVisibility(View.GONE);
                                searchingPrompt.setText("Searing for an opponent...");
                                Player addPlayer = new Player(id, name, surname, mKey);
                                mReference.child("lobby_users").child(mKey).setValue(addPlayer);
                                Intent i = getIntent();
                                finish();
                                overridePendingTransition( 0, 0);
                                startActivity(i);
                                overridePendingTransition( 0, 0);
                            }
                        });
                        noButton.setVisibility(View.VISIBLE);
                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                logoutButton.performClick();
                            }
                        });
                    }
                });
            }

        }, 10000);
    }
}

package csci201.han.edward.fi.draw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class LoginGUI extends FragmentActivity{

    private Button loginButton;
    private Button createButton;
    private Button sandboxButton;

    private EditText usernameEdit;
    private EditText passwordEdit;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private ImageView dragonView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_gui);

        loginButton = (Button) findViewById(R.id.login_button);
        createButton = (Button) findViewById(R.id.create_account_btn);
        sandboxButton = (Button) findViewById(R.id.sandbox_button);

        usernameEdit = (EditText) findViewById(R.id.editText);
        passwordEdit = (EditText) findViewById(R.id.editText2);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        dragonView = (ImageView) findViewById(R.id.dragonImg);

        addListeners();

    }

    public void addListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEdit.getText().toString();
                final String password = passwordEdit.getText().toString();

                mReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        for(DataSnapshot c : dataSnapshot.getChildren()) {
                            if(c.getKey().equals(username)) {
                                if(c.child("lastName").getValue().equals(password)) {
                                    exists = true;
                                    Player p = new Player(username, username, password, username);
                                    mReference.child("lobby_users").child(username).setValue(p);
                                    p.setMatched("false");
                                    p.setSecond(false);


                                    WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                                    String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                                    p.setAddress(ip);
                                    mReference.child("users").child(username).child("address").setValue(ip);
                                    mReference.child("users").child(username).child("match").setValue("false");
                                    mReference.child("users").child(username).child("opponentKey").setValue("none");
                                    Intent lobby = new Intent(LoginGUI.this, LobbyActivity.class);
                                    lobby.putExtra("name", username);
                                    lobby.putExtra("surname", password);
                                    lobby.putExtra("id", username);
                                    lobby.putExtra("key", username);
                                    startActivity(lobby);
                                }
                            }
                        }
                        if(!exists) {
                            Toast.makeText(getApplicationContext(), "This account does not exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameEdit.getText().toString();
                final String password = passwordEdit.getText().toString();

                mReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        for(DataSnapshot c : dataSnapshot.getChildren()) {
                            if(c.getKey().equals(username)) {
                                exists = true;
                                Toast.makeText(getApplicationContext(), "This username already exists", Toast.LENGTH_LONG).show();
                            }
                        }

                        if(username.equals("") || password.equals("")) {
                            exists = true;
                            Toast.makeText(getApplicationContext(), "Invalid account information", Toast.LENGTH_LONG).show();
                        }
                        if(!exists) {
                            Player p = new Player(username, username, password, username);
                            mReference.child("users").child(username).setValue(p);
                            mReference.child("lobby_users").child(username).setValue(p);
                            Intent lobby = new Intent(LoginGUI.this, LobbyActivity.class);
                            lobby.putExtra("name", username);
                            lobby.putExtra("surname", password);
                            lobby.putExtra("id", username);
                            lobby.putExtra("key", username);
                            startActivity(lobby);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        sandboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sandboxIntent = new Intent(LoginGUI.this, SandboxCanvas.class);
                startActivity(sandboxIntent);
            }
        });
    }


}


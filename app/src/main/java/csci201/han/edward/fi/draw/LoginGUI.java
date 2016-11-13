package csci201.han.edward.fi.draw;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginGUI extends FragmentActivity{

    private LoginButton loginButton;
    private Button sandboxButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private FirebaseDatabase database;
    private DatabaseReference mReference;


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            mReference.child("users").child(profile.getId()).setValue(profile.getFirstName() + " " + profile.getLastName());
            mReference.child("lobby_users").child(profile.getId()).setValue(profile.getFirstName() + " " + profile.getLastName());
            mReference.child("numberOfUsers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = String.valueOf(dataSnapshot.getValue());
                    long count = Long.parseLong(value) + 1;
                    mReference.child("numberOfUsers").setValue(count);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            nextActivity(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    //any other instance or member variables here:
        //TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login_gui);

        database = FirebaseDatabase.getInstance();
        mReference = database.getReference();

        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //String value = dataSnapshot.getValue(String.class);
                //long count = Long.parseLong(dataSnapshot.getValue(String.class)) + 1;
                //String temp = "" + count + "";
                //mReference.child("numberOfUsers").setValue(temp);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                nextActivity(currentProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        sandboxButton = (Button) findViewById(R.id.sandbox_button);
        sandboxButton.getBackground().setColorFilter(0xFF3b5998 , PorterDuff.Mode.MULTIPLY);

        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                mReference.child("users").child(profile.getId()).setValue(profile.getFirstName() + " " + profile.getLastName());
                mReference.child("lobby_users").child(profile.getId()).setValue(profile.getFirstName() + " " + profile.getLastName());
                mReference.child("numberOfUsers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = String.valueOf(dataSnapshot.getValue());
                        long count = Long.parseLong(value) + 1;
                        mReference.child("numberOfUsers").setValue(count);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                nextActivity(profile);
                //Toast.makeText(getApplicationContext(), "Logging in as " + profile.getFirstName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    private void nextActivity(Profile profile) {
        if(profile != null) {
            Intent main = new Intent(LoginGUI.this, MainActivity.class);
            main.putExtra("name", profile.getFirstName());
            main.putExtra("surname", profile.getLastName());
            main.putExtra("id", profile.getId());
            startActivity(main);
        }
    }

    public void logout() {
        LoginManager.getInstance().logOut();
    }
}

package csci201.han.edward.fi.draw;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
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
        //key = inBundle.get("key").toString();

        //Player player = new Player(id, name, surname, key);
        welcomePrompt.setText("Welcome " + name + " " + surname);
        idPrompt.setText("Your id is " + id);

        mReference.child("lobby_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numChildren = (int)dataSnapshot.getChildrenCount();
                System.out.println("Number of children in tree im looking at: " + numChildren);
                if(numChildren != 0 && numChildren != 1) {
                    isSecondUser = true;
                }

                if(isSecondUser) {
                    int counter = 0;
                    for(DataSnapshot c : dataSnapshot.getChildren()) {
                        if(counter == 0) {
                            String value = (String)c.child("uid").getValue();
                            String key = (String) c.getKey();
                            mReference.child("users").child(value).child("opponentKey").setValue(id);
                            mReference.child("users").child(id).child("opponentKey").setValue(value);
                            mReference.child("lobby_users").child(key).removeValue();
                        }
                        if (counter == 1) {
                            mReference.child("lobby_users").child(c.getKey()).removeValue();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mReference.child("lobby_users").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                key = dataSnapshot.getKey();
//                if(isSecondUser) {
//                    isSecondUser = false;
//                    //set the opponent of the users in the users tree
//                    mReference.child("lobby_users").child(s).child("uid").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            String value = (String)dataSnapshot.getValue();
//                            mReference.child("users").child(value).child("opponentKey").setValue(id);
//                            mReference.child("users").child(id).child("opponentKey").setValue(value);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                    //remove each other from the lobby_users tree
//                    mReference.child("lobby_users").child(s).removeValue();
//                    mReference.child("lobby_users").child(key).removeValue();
//                }
//                else {
//                    System.out.println("Only user in the tree");
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                //String value = dataSnapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


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

//                if(key != null) {
//                    if (mReference.child("lobby_users") != null) {
//                        if (mReference.child("lobby_users") != null && mReference.child("lobby_users").child(key) != null) {
//                            mReference.child("lobby_users").child(key).removeValue();
//
//                            mReference.child("users").child(id).child("opponentKey").addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    String value = (String) dataSnapshot.getValue();
//                                    System.out.println("The data snapshot value is: " + value);
//                                    if(!value.equals("none")) {
//                                        mReference.child("users").child(value).child("opponentKey").setValue("none");
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//
//                            //now set my opponent key to empty
//                            mReference.child("users").child(id).child("opponentKey").setValue("none");
//                        }
//                    }
//                }

                Intent login = new Intent(MainActivity.this, LoginGUI.class);
                startActivity(login);
                finish();
            }
        });

    }
}

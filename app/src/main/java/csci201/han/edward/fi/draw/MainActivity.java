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
        Player player = new Player(id, name, surname);
        welcomePrompt.setText("Welcome " + name + " " + surname);
        idPrompt.setText("Your id is " + id);

        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //String value = dataSnapshot.getValue(String.class);
                //DataSnapshot[] temp = dataSnapshot.getChildren();
                pair = pair(dataSnapshot);

                if(pair != -1) {
                    String opponent;
                    if(pair == 0) {
                        int count = 0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if(count == 1) {
                                opponent = data.getKey();
                            }
                        }
                    }
                    if(pair == 1) {
                        int count = 0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if(count == 0) {
                                opponent = data.getKey();
                            }
                        }
                    }
                    mReference.child("lobby_users").child(id).removeValue();
                }

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





        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                mReference.child("users").child(id).removeValue();
                mReference.child("lobby_users").child(id).removeValue();
                mReference.child("numberOfUsers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = String.valueOf(dataSnapshot.getValue());
                        long count = Long.parseLong(value) - 1;
                        mReference.child("numberOfUsers").setValue(count);
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
    public int pair(DataSnapshot dataSnapshot) {
        System.out.println("Entered the pair function");
        int count = 0;
        System.out.println("children count: " + dataSnapshot.getChildrenCount());
        if(dataSnapshot.getChildrenCount() < 2) {
            System.out.println("returning -1 a");
            return -1;
        }
        for (DataSnapshot data : dataSnapshot.getChildren()) {
            if (count > 1) {
                System.out.println("returning -1 b");
                return -1;
            }

            if (data.getKey().equals(id)) {
                System.out.println("returning " + count);
                return count;
            }

            count++;
        }
        System.out.println("returning -1 c");
        return -1;
    }

}

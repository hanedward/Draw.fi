package csci201.han.edward.fi.draw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public Button logoutButton;
    public TextView welcomePrompt;
    public TextView idPrompt;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
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
        welcomePrompt.setText("Welcome " + name + " " + surname);
        idPrompt.setText("Your id is " + id);



        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                mReference.child("users").child(id).removeValue();
                mReference.child("lobby_users").child(id).removeValue();
                Intent login = new Intent(MainActivity.this, LoginGUI.class);
                startActivity(login);
                finish();
            }
        });

    }

}

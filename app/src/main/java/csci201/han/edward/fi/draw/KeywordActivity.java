package csci201.han.edward.fi.draw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KeywordActivity extends AppCompatActivity {

    private TextView mKeywordView;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    String player1;
    String player2;
    String mKeyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword);

        Bundle inBundle = getIntent().getExtras();
        player1 = inBundle.get("player1").toString();
        player2 = inBundle.get("player2").toString();

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mKeywordView = (TextView) findViewById(R.id.keword_holder);


        mReference.child("users").child("keyword").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mKeyword = (String)dataSnapshot.getValue();
                mKeywordView.setText(mKeyword);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

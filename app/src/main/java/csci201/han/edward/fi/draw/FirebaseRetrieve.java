package csci201.han.edward.fi.draw;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRetrieve {
    public static Object get(final DatabaseReference databaseReference){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return null;

    }

//    private DatabaseReference databaseReference;
//    private Object buffer;
//
//    public  FirebaseRetrieve(DatabaseReference databaseReference){
//        this.databaseReference = databaseReference;
//    }
//
//
//    public void retrieve(Object buffer){
//        this.buffer
//    }


}
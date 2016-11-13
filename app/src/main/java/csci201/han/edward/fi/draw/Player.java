package csci201.han.edward.fi.draw;

import java.io.Serializable;

/**
 * Created by AlecFong on 11/12/16.
 */

public class Player implements Serializable{

    private String uid;
    private String firstName;
    private String lastName;
    private String oponentId;

    public Player(String id, String fName, String lName){
        uid = id;
        firstName = fName;
        lastName = lName;
    }

    public String getUid(){
        return uid;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setFirstName(String name){
        firstName = name;
    }

    public void setLastName(String name){
        lastName = name;
    }

    public void setOponentId(String id){
        oponentId = id;
    }

    private void resetOponent(){
        oponentId = null;
    }
}
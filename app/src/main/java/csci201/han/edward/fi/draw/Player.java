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
    String key;

    public Player(String id, String fName, String lName, String key){
        uid = id;
        firstName = fName;
        lastName = lName;
        this.key = key;
    }

    public String getUid(){
        return uid;
    }

    public String getKey() {
        return key;
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
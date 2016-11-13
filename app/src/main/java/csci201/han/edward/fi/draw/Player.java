package csci201.han.edward.fi.draw;

import java.io.Serializable;


public class Player implements Serializable{

    private String uid;
    private String firstName;
    private String lastName;
    private String opponentKey;
    private String mKeyword;
    String key;

    public Player(String id, String fName, String lName, String key){
        uid = id;
        firstName = fName;
        lastName = lName;
        this.key = key;
        mKeyword = "";
        opponentKey = "none";
    }

    public String getUid(){
        return uid;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
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

    public void setOpponentKey(String id){
        opponentKey = id;
    }

    public String getOpponentKey() {
        return opponentKey;
    }

    private void resetOpponent(){
        opponentKey = "none";
    }
}
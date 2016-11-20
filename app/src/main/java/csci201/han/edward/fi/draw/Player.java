package csci201.han.edward.fi.draw;

import java.io.Serializable;


public class Player implements Serializable{

    private String uid;
    private String firstName;
    private String lastName;
    private String opponentKey;
    private String mKeyword;
    private String matched;
    private Boolean isSecond;
    private int totalScore;
    private String ipAddress;
    String key;

    public Player(String id, String fName, String lName, String key){
        uid = id;
        firstName = fName;
        lastName = lName;
        this.key = key;
        mKeyword = "";
        opponentKey = "none";
        matched = "false";
        isSecond = false;
        totalScore = 0;
        ipAddress = "";
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

    public void setMatched(String match) {
        matched = match;
    }

    public String getMatch() {
        return matched;
    }

    public void setSecond(boolean s) {
        isSecond = s;
    }

    public boolean getSecond() {
        return isSecond;
    }

    public void setScore(int s) {
        totalScore = s;
    }

    public int getScore() {
        return totalScore;
    }

    public void setAddress(String a) {
        ipAddress = a;
    }

    public String getAddress() {
        return ipAddress;
    }
}
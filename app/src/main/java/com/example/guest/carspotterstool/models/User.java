package com.example.guest.carspotterstool.models;

import java.util.ArrayList;

/**
 * Created by Guest on 6/19/17.
 */

public class User {
    private String name;
    private String leaderBoardStanding;
    private String imageUser;
    private String pushId;
    private String score;
    private ArrayList<String> cars;

    public User(){}

    public String getName() {
        return name;
    }

    public String getLeaderBoardStanding() {
        return leaderBoardStanding;
    }

    public String getImageUser() {
        return imageUser;
    }

    public String getPushId() { return pushId; }

    public void setPushId(String pushId) { this.pushId = pushId; }

    public String getScore() {
        return score;
    }

    public ArrayList<String> getCars() {
        return cars;
    }

    public User(String name, String leaderBoardStanding, String imageUser, String score){
        this.name = name;
        this.leaderBoardStanding = leaderBoardStanding;
        this.imageUser = imageUser;
        this.score = score;
    }
}

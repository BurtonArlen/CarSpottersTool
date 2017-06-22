package com.example.guest.carspotterstool.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Guest on 6/19/17.
 */

@Parcel
public class User {
    private String name;
    private String uid;
    private String leaderBoardStanding;
    private String imageUser;
    private String pushId;
    private String score;
    private String contributionCount;
    private ArrayList<String> cars;

    public User(){}

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getLeaderBoardStanding() {
        return leaderBoardStanding;
    }

    public void setLeaderBoardStanding(String leaderBoardStanding) { this.leaderBoardStanding = leaderBoardStanding; }

    public String getImageUser() {
        return imageUser;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getContributionCount(){
        return contributionCount;
    }

    public void setContributionCount(String contributionCount){
        this.contributionCount = contributionCount;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score){
        this.score = score;
    }

    public ArrayList<String> getCars() {
        return cars;
    }

    public User(String name, String imageUser, String uid){
        this.name = name;
        this.leaderBoardStanding = "not_set";
        this.imageUser = imageUser;
        this.score = "not_set";
        this.contributionCount = "not_set";
        this.uid = uid;
    }
}

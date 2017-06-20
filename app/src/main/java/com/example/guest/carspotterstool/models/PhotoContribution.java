package com.example.guest.carspotterstool.models;

import org.parceler.Parcel;

/**
 * Created by Guest on 6/19/17.
 */
@Parcel
public class PhotoContribution {
    private String imageEncoded;
    private String pushId;
    private String year;
    private String make;
    private String model;
    private String submitterName;
    private String submitterId;

    public PhotoContribution (){}

    public PhotoContribution (String imageEncoded, String year, String make, String model, String submitterName, String submitterId){
        this.imageEncoded = "url";
        this.year = "unknown";
        this.make = "unknown";
        this.model = "unknown";
        this.submitterName = "unknown";
        this.submitterId = "unknown";
    }

    public void setSubmitterId(String submitterId){
        this.submitterId = submitterId;
    }

    public String getSubmitterId(){
        return submitterId;
    }

    public void setSubmitterName(String submitterName){
        this.submitterName = submitterName;
    }

    public String getSubmitterName(){
        return submitterName;
    }

    public void setImageEncoded(String imageEncoded) { this.imageEncoded = imageEncoded; }

    public String getImageEncoded(){ return imageEncoded; }

    public String getPushId() { return pushId; }

    public void setPushId(String pushId) { this.pushId = pushId; }

    public String getYear(){
        return year;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
    public void setYear(String year){
        this.year = year;
    }
    public void setMake(String make){
        this.make = make;
    }
    public void setModel(String model){
        this.model = model;
    }
}

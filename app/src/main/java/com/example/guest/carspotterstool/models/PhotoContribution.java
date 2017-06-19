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

    public PhotoContribution (){}

    public PhotoContribution (String imageEncoded, String year, String make, String model){
        this.imageEncoded = "url";
        this.year = "unknown";
        this.make = "unknown";
        this.model = "unknown";
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

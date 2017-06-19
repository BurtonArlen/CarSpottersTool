package com.example.guest.carspotterstool.models;

import org.parceler.Parcel;

/**
 * Created by Guest on 6/19/17.
 */
@Parcel
public class PhotoContribution {
    private String imageEncoded;
    private String pushId;

    public PhotoContribution (){}

    public PhotoContribution (String imageEncoded){
        this.imageEncoded = "url";
    }

    public void setImageEncoded(String imageEncoded) { this.imageEncoded = imageEncoded; }

    public String getImageEncoded(){ return imageEncoded; }

    public String getPushId() { return pushId; }

    public void setPushId(String pushId) { this.pushId = pushId; }
}

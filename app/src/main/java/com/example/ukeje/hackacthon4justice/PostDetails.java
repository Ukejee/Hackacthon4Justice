package com.example.ukeje.hackacthon4justice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDetails {

    @SerializedName("a")
    @Expose
    private String distress;
    @SerializedName("la")
    @Expose
    private double latitude;
    @SerializedName("lo")
    @Expose
    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }



    public String getDistress() {
        return distress;
    }

    public void setDistress(String distress) {
        this.distress = distress;
    }

}

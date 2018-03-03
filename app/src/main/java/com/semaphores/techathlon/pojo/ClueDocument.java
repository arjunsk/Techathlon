package com.semaphores.techathlon.pojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ClueDocument {

    private String key;
    private double lat;
    private double lon;
    private String description;
    private boolean isImage;
    private boolean isLocked;

    public ClueDocument(){
        super();
        this.lat=0.0;
        this.lon = 0.0;
        this.description="";
        this.isLocked = true;
        this.isImage = false;
    }

    public ClueDocument(String description, boolean isImage, double lat, double lon){
        this.lat=lat;
        this.lon = lon;
        this.description=description;
        this.isImage = isImage;
        this.isLocked = true;
    }


    //For Firebase Object Key
    public String getKey() { return key; }
    public void setKey(String key) {
        this.key = key;
    }



    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public String getDescription() {
        return description;
    }
    public boolean isImage() {
        return isImage;
    }
    public boolean isLocked() {
        return isLocked;
    }



    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public void setDescription(String next_clue) {
        this.description = next_clue;
    }

    public void setImage(boolean isImage)
    {
        this.isImage = isImage;
    }

    public void setLocked(boolean isLocked)
    {
        this.isLocked = isLocked;
    }

}

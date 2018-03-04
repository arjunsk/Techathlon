package com.semaphores.techathlon.pojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ClueDocument {

    private String key;
    private double lat;
    private double lon;
    private String description;
    private int is_image;
    private boolean isLocked;

    public ClueDocument(){
        super();
        this.lat=0.0;
        this.lon = 0.0;
        this.description="";
        this.isLocked = true;
        this.is_image = 0;
    }

    public ClueDocument(String description, int is_image, double lat, double lon){
        this.lat=lat;
        this.lon = lon;
        this.description=description;
        this.is_image = is_image;
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

    public int getIs_image()
    {
        return is_image;
    }

    public void setIs_image(int is_image)
    {
        this.is_image = is_image;
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

    public void setLocked(boolean isLocked)
    {
        this.isLocked = isLocked;
    }

}

package com.semaphores.techathlon.pojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ClueDocument
{

    private String key;
    private boolean endpoint;
    private int index;
    private double lat;
    private double lon;
    private String next_clue;

    public ClueDocument(){
        super();
        this.endpoint=true;
        this.index = 0;
        this.lat=0.0;
        this.lon = 0.0;
        this.next_clue="";
    }

    public ClueDocument(Boolean endpoint, Integer index, Double lat, Double lon, String next_clue){
        this.endpoint=endpoint;
        this.index = index;
        this.lat=lat;
        this.lon = lon;
        this.next_clue=next_clue;
    }


    //For Firebase Object Key
    public String getKey() { return key; }
    public void setKey(String key) {
        this.key = key;
    }


    public boolean getEndpoint() {
        return endpoint;
    }
    public int getIndex() {
        return index;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public String getNext_clue() {
        return next_clue;
    }


    public void setEndpoint(boolean endpoint) {
        this.endpoint = endpoint;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public void setNext_clue(String next_clue) {
        this.next_clue = next_clue;
    }
}

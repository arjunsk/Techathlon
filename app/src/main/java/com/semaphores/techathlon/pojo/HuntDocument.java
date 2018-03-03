package com.semaphores.techathlon.pojo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class HuntDocument
{

    private String key;
    private String host;
    private int leading_clue;
    private boolean live;
    private String name;
    private String prize;
    private String start_clue;

    public HuntDocument(){
        super();
        this.host="";
        this.leading_clue = 0;
        this.live=true;
        this.name="";
        this.prize = "";
        this.start_clue="";
    }

    public HuntDocument(String host, Integer leading_clue, Boolean live, String name, String prize, String start_clue){
        this.host=host;
        this.leading_clue = leading_clue;
        this.live=live;
        this.name=name;
        this.prize = prize;
        this.start_clue=start_clue;
    }

    //For Firebase Object Key
    public String getKey() { return key; }
    public void setKey(String key) {
        this.key = key;
    }


    public String getHost() {
        return host;
    }
    public int getLeading_clue() {
        return leading_clue;
    }
    public boolean getLive() {
        return live;
    }
    public String getName() {
        return name;
    }
    public String getPrize() {
        return prize;
    }
    public String getStart_clue() {
        return start_clue;
    }

    public void setHost(String host) {
        this.host = host;
    }
    public void setLeading_clue(int leading_clue) {
        this.leading_clue = leading_clue;
    }
    public void setLive(boolean live) {
        this.live = live;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrize(String prize) {
        this.prize = prize;
    }
    public void setStart_clue(String start_clue) {
        this.start_clue = start_clue;
    }
}

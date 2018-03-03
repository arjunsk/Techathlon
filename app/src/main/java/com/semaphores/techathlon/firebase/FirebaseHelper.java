package com.semaphores.techathlon.firebase;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {


    public static DatabaseReference getRef_contests(){
        return FirebaseDatabase.getInstance().getReference("/contests");
    }
    public static DatabaseReference getRef_clues(){
        return FirebaseDatabase.getInstance().getReference("/clues");
    }

    public static DatabaseReference getRef_contestants(){
        return FirebaseDatabase.getInstance().getReference("/contestants");
    }

    public static DatabaseReference getRef_leaderboard(){
        return FirebaseDatabase.getInstance().getReference("/leaderboard");
    }


}
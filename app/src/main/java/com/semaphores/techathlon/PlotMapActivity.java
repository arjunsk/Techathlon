package com.semaphores.techathlon;

import android.*;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.semaphores.techathlon.firebase.FirebaseHelper;
import com.semaphores.techathlon.pojo.ClueDocument;
import com.semaphores.techathlon.pojo.HuntDocument;

import java.util.HashMap;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class PlotMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Google Maps
    MapView mMapView;
    private GoogleMap googleMap;

    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetPlotMap;

    private FloatingActionButton fabButton;
    EditText clue_description;
    TextView tv_clue_index;

    HashMap<Integer,ClueDocument> hmap_clues;
    HashMap<Integer,Marker> hmap_markers;
    int clue_index;

    //Firebase
    DatabaseReference clues_collection;
    DatabaseReference contests_collection;


    private @DrawableRes int fab_icons[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_map);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FAFAFA'>" + "Plot clues" + "</font>"));
        initView();
        initBottomSheet();

        //Firebase
        clues_collection = FirebaseHelper.getRef_clues();
        contests_collection = FirebaseHelper.getRef_contests();

        //Hash map
        hmap_clues  = new HashMap<>();
        hmap_markers  = new HashMap<>();

        clue_index=1;

        //MAP
        mMapView = findViewById(R.id.plot_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        initialize_fab_icons();

    }

    private void initialize_fab_icons() {
        fab_icons = new int[]{R.drawable.ic_add_24dp,  // plus button
                              R.drawable.ic_check_black_24dp, // tick button
                              R.drawable.ic_edit_black_24dp // edit button
        };

    }

    public void onFabClick(View view){

        if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED) {
           // If fab button was add--> change this to tick
           fabButton.setImageResource(R.drawable.ic_check_black_24dp);

           // Open the enter clue form
           bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

       }else  if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {
           //get content from the text box;
            String desc = clue_description.getText().toString();
           // check if the content is valid ie not null
            if(desc.isEmpty()){
               // return;
            }

            ClueDocument clue = new ClueDocument(desc, 0, googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
           //Push to the Hashmap
            hmap_clues.put(clue_index,clue);

            //Add marker to google maps
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(clue.getLat(), clue.getLon()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_treasure))
            );

           // marker.

           //Push to the Hash of marker
            hmap_markers.put(clue_index,marker);

           fabButton.setImageResource(R.drawable.ic_add_24dp);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            clue_description.setText("");

            tv_clue_index.setText("Clue #"+ ++clue_index);
       }

    }

    public  void save_clues(){

        new MaterialDialog.Builder(this)
                .title("Contest Details")
                .customView(R.layout.drawer_contest_details, true)
                .positiveText("OK")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        // TODO

                        EditText input_name = (EditText) dialog.getView().findViewById(R.id.dailog_save_name_etv);
                        EditText input_prize = (EditText) dialog.getView().findViewById(R.id.dailog_save_prize_etv);

                        String contest_name  = input_name.getText().toString();
                        String contest_prize = input_prize.getText().toString();

                        HuntDocument contest_data  = new HuntDocument("userid1",0,true,contest_name,contest_prize,"");

                        contests_collection.push().setValue(contest_data, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                String contest_id = databaseReference.getKey();

                                for (ClueDocument value : hmap_clues.values()) {
                                    clues_collection.child(contest_id).push().setValue(value);
                                }
                                hmap_clues.clear();

                                Intent intent=new Intent(getApplicationContext(), ChooseHuntActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        });


                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        // TODO
                        dialog.dismiss();
                    }
                })
                .build()
                .show();



    }


    public void initView()
    {
        bottomSheetPlotMap = findViewById(R.id.bottom_sheet_plot_map);
        fabButton = findViewById(R.id.bottom_sheet_add_clue_button);
        clue_description = findViewById(R.id.bottom_sheet_add_clue_edit_text);
        tv_clue_index = findViewById(R.id.tv_clue_index);
    }

    public void initBottomSheet()
    {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetPlotMap);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onMapReady(final GoogleMap gm) {

        googleMap= gm;
        // Showing / hiding your current location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);


        SmartLocation.with(this)
        .location()
        .start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
            LatLng curr_location = new LatLng(location.getLatitude(),location.getLongitude());

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(curr_location));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_button: {
                save_clues();
                break;
            }
            // case blocks for other MenuItems (if any)
        }
        return false;
    }
}

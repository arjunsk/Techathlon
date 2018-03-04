package com.semaphores.techathlon

import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.semaphores.techathlon.Adapters.ClueListAdapter
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.clue.view.*
import kotlinx.android.synthetic.main.clue_image.view.*
import kotlinx.android.synthetic.main.clue_number.view.*
import kotlinx.android.synthetic.main.clues.*
import com.google.firebase.database.DatabaseReference
import com.semaphores.techathlon.firebase.FirebaseHelper
import com.semaphores.techathlon.pojo.ClueDocument
import kotlinx.android.synthetic.main.activity_treasure_map.*
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.location.config.LocationParams
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.semaphores.techathlon.utils.Helper

class TreasureMapActivity : AppCompatActivity(), OnMapReadyCallback
{
    val TAG = "TreasureMapActivity"
    var currentClue = -1
    val clueList = mutableListOf<ClueDocument>()
    lateinit var clueListAdapter: ClueListAdapter
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var layoutManager: LinearLayoutManager

    lateinit var huntsCollection: DatabaseReference
    lateinit var cluesCollection: DatabaseReference
    lateinit var mapView: MapView
    lateinit var googleMap: GoogleMap

    var zoomedInFirstTime: Boolean = false
    var markers: HashMap<String, Marker> = hashMapOf()

    var contestID = "contest0"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_map)
        Helper.init_loading_dialog(this);

        init()
        initMap(savedInstanceState)
        initUI()
        initClues()
        initRecyclerView()
        initListener()
    }

    fun init()
    {
        contestID = intent.getStringExtra("HUNT_ID")
        huntsCollection = FirebaseHelper.getRef_contests();
        cluesCollection = FirebaseHelper.getRef_clues();
    }

    fun initMap(savedInstanceState: Bundle?)
    {
        mapView = findViewById(R.id.treasure_map)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        try
        {
            MapsInitializer.initialize(this@TreasureMapActivity)
        }catch (e: Exception)
        {
            e.printStackTrace()
        }

        mapView.getMapAsync(this@TreasureMapActivity)
    }

    fun initUI()
    {
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        val scale = resources.displayMetrics.density
        val dpAsPixels = (24 * scale + 0.5f)
        bottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback()
        {
            override fun onSlide(bottomSheet: View, slideOffset: Float)
            {
                bottomSheet.setPadding(0, (dpAsPixels * slideOffset).toInt(), 0, 0)
            }

            override fun onStateChanged(bottomSheet: View, newState: Int)
            {
                when (newState)
                {
                    BottomSheetBehavior.STATE_EXPANDED ->
                    {
                        clue_test_add.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED ->
                    {
                        clue_test_add.visibility = View.GONE
                    }
                }
            }
        })
    }


    fun initRecyclerView()
    {
        clueListAdapter = ClueListAdapter(clueList)
        clues_list_recycler_view.adapter = clueListAdapter
        layoutManager = LinearLayoutManager(this@TreasureMapActivity)
        clues_list_recycler_view.layoutManager = layoutManager
    }

    fun initClues()
    {
        cluesCollection.child(contestID).addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                Log.i(TAG, "Database size ${dataSnapshot.children.count()}")
                for (cluesSnapshot in dataSnapshot.children)
                {
                    val clue = cluesSnapshot.getValue<ClueDocument>(ClueDocument::class.java)
                    clue!!.setKey(cluesSnapshot.key)
                    if (clue.is_image==1)
                        Log.i(TAG, "clue isimage")
                    clueList.add(clue)
                }


                loadMap()
                startLocationListener()
                //unlockNextClue()
                onNewClueReceived()
                Log.i(TAG, "Total clues ${clueList.size}")
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                Log.e(TAG, "Data Fetch Cancelled")
            }
        })
    }

    fun initListener()
    {
        clue_test_add.setOnClickListener { unlockNextClue() }
        fab.setOnClickListener { onNewClueReceived() }
    }

    fun unlockNextClue()
    {
        if (currentClue < (clueList.size - 1))
        {
            currentClue += 1
            clueList[currentClue].isLocked = false

            if (clueList[currentClue].isLocked)
                Log.i(TAG, "clue $currentClue is locked")
            else
                Log.i(TAG, "clue $currentClue is unlocked")
            //clueList[currentClue].time = getCurrentTime()
            // Only refresh the next clue view
            clueListAdapter.notifyItemChanged(currentClue)
            updateClueProgress()
        }
        else if (currentClue == clueList.size - 1)
        {
            changeMarkerIcon("TreasureBox");
            Toast.makeText(this@TreasureMapActivity, "Last clue",Toast.LENGTH_SHORT).show()
        }
        Log.i(TAG, "Current clue $currentClue")
    }

    fun changeMarkerIcon(markerStyle: String)
    {
        @DrawableRes var resource: Int = R.drawable.marker3_purple

        if(markerStyle.equals("Treasure")){
            resource = R.drawable.marker_treasure
        }else if(markerStyle.equals("UnlockedClue")){
            resource = R.drawable.marker1_yellow
        }
        markers.get(""+currentClue)?.setIcon(BitmapDescriptorFactory.fromResource(resource))
    }

    fun updateClueProgress()
    {
        clues_progress.text = String.format(getString(R.string.clues_progress), (currentClue + 1), clueList.size)
    }

    fun onNewClueReceived()
    {
        unlockNextClue()
        val dialogBuilder = AlertDialog.Builder(this@TreasureMapActivity)
        var clueView : View = layoutInflater.inflate(R.layout.clue, null)
        with (clueList[currentClue])
        {
            if (is_image == 1)
            {
                clueView = layoutInflater.inflate(R.layout.clue_image, null)
                when (currentClue)
                {
                    0 -> clueView.clue_image.setImageResource(R.drawable.clue_2)
                    1 -> clueView.clue_image.setImageResource(R.drawable.clue_3)
                    2 -> clueView.clue_image.setImageResource(R.drawable.clue_5)
                    else -> clueView.clue_image.setImageResource(R.drawable.clue_8)
                }
            }
            else
                clueView.clue_description.text = description

            clueView.clue_number.text = (currentClue + 1).toString()
        }
        dialogBuilder.setView(clueView)
        val clueDialog = dialogBuilder.create()
        clueDialog.window.attributes.windowAnimations = R.style.DialogAnimationStyle
        clueDialog.show()
    }

    //------------------XXXXXXXXXXXXXXXX Google Maps XXXXXXXXXX----------------


    public override fun onResume()
    {
        super.onResume()
        mapView.onResume()
        startLocationListener()
    }

    public override fun onPause()
    {
        super.onPause()
        mapView.onPause()
        SmartLocation.with(this).location().stop()
    }

    public override fun onDestroy()
    {
        super.onDestroy()
        mapView.onDestroy()
        SmartLocation.with(this).location().stop()
    }

    override fun onLowMemory()
    {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?)
    {
        Helper.show_loading_dialog();
        googleMap = p0!!
        googleMap.setMyLocationEnabled(true);
    }

    private fun loadMap()
    {

        var clued_index = 0
        for (clue in clueList)
        {

            val marker = googleMap.addMarker(MarkerOptions()
                    .position(LatLng(clue.getLat(), clue.getLon()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1_yellow))
            )

            marker.isVisible = false

            markers.put("" + clued_index, marker)
            clued_index++
        }
    }

    //----------------------------XXXXXXXX  SMART LOCATION XXXXXXXXXXXX------------//
    private fun startLocationListener()
    {
        SmartLocation.with(this)
                .location()
                .continuous()
                .config(LocationParams.NAVIGATION)
                .start(object : OnLocationUpdatedListener
                {
                    override fun onLocationUpdated(location: Location)
                    {
                        processLocation(location)
                    }
                })
    }

    private fun processLocation(location: Location)
    {
        if( markers==null || markers.isEmpty() ) return;

        //STEP 1 -- UPDATE Location in the MAP -- handled on top
        val latLng: LatLng = LatLng(location.getLatitude(), location.getLongitude());

        //Travel Perspective
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        if(!zoomedInFirstTime) {
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18f));// Move Camera before zoom
            zoomedInFirstTime=true;
        }

        //STEP 2 -- Calculate distance between current location & next clue
        val displacement: Float = distance_between(latLng.latitude, latLng.longitude,
        clueList.get(currentClue).getLat(),
        clueList.get(currentClue).getLon());

        //Log.e(TAG,"Displacement"+displacement);

        if(displacement<=20){
            //set visible the marker
            markers.get(""+currentClue)!!.setVisible(true);
        }

        if(displacement<=20){
            onNewClueReceived()
        }

        Helper.hide_loading_dialog();
    }

    private fun distance_between(x1: Double, y1: Double, x2: Double, y2: Double): Float
    {
        val loc1 = Location("")
        loc1.latitude = x1
        loc1.longitude = y1

        val loc2 = Location("")
        loc2.latitude = x2
        loc2.longitude = y2

        return loc1.distanceTo(loc2)
    };
}


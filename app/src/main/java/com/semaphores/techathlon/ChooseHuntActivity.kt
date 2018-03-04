package com.semaphores.techathlon

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import com.semaphores.gofind.Adapters.HuntListAdapter
import kotlinx.android.synthetic.main.activity_choose.*
import kotlinx.android.synthetic.main.hunts.*
import com.google.firebase.database.DatabaseReference
import com.semaphores.techathlon.firebase.FirebaseHelper
import com.semaphores.techathlon.pojo.HuntDocument
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.util.Log
import com.semaphores.techathlon.utils.Helper


class ChooseHuntActivity : AppCompatActivity()
{
    val TAG = "ChooseHuntActivity"
    val huntList = mutableListOf<HuntDocument>()
    lateinit var huntListAdapter: HuntListAdapter
    lateinit var huntsCollection: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        Helper.init_loading_dialog(this);
        Helper.show_loading_dialog()

        init()
        initUI()
        initHunts()
        initRecyclerView()
        initListeners()
    }

    fun init()
    {
        huntsCollection = FirebaseHelper.getRef_contests()
    }

    fun initHunts()
    {
        //addSampleHunts()
        huntList.clear()

        huntsCollection.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {

                for (contestSnapshot in dataSnapshot.children)
                {
                    val contest = contestSnapshot.getValue(HuntDocument::class.java)
                    contest!!.key = contestSnapshot.key
                    huntList.add(contest)
                }

                Helper.hide_loading_dialog()
                initRecyclerView()
            }

            override fun onCancelled(databaseError: DatabaseError)
            {
                Log.e(TAG, "Data Fetch Cancelled")
            }
        })
    }

    fun initUI()
    {
        supportActionBar?.setTitle(Html.fromHtml("<font color='#FAFAFA'>" + getString(R.string.app_name) + "</font>"))
    }

    fun initRecyclerView()
    {
        huntListAdapter = HuntListAdapter(huntList,
                object: HuntListAdapter.onClickShare
                {
                    override fun invoke(hunt: HuntDocument)
                    {
                    }

                },
                object: HuntListAdapter.onClickJoin
                {
                    override fun invoke(hunt: HuntDocument)
                    {
                        val intent = Intent(this@ChooseHuntActivity, TreasureMapActivity::class.java)
                        intent.putExtra("HUNT_ID", hunt.key);
                        startActivity(intent);
                    }

                })

        hunts_list_recycler_view.adapter = huntListAdapter
        hunts_list_recycler_view.layoutManager = LinearLayoutManager(this@ChooseHuntActivity)
    }

    fun initListeners()
    {
        fab.setOnClickListener { startActivity(Intent(this@ChooseHuntActivity, PlotMapActivity::class.java)) }
    }
}

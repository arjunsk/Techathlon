package com.semaphores.techathlon

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import com.semaphores.gofind.Adapters.HuntListAdapter
import com.semaphores.techathlon.Models.Hunt
import kotlinx.android.synthetic.main.activity_choose.*
import kotlinx.android.synthetic.main.hunts.*

class ChooseHuntActivity : AppCompatActivity()
{
    val TAG = "ChooseHuntActivity"
    val huntList = mutableListOf<Hunt>()
    lateinit var huntListAdapter: HuntListAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        initUI()
        initHunts()
        initRecyclerView()
        initListeners()
    }

    fun initUI()
    {
        supportActionBar?.setTitle(Html.fromHtml("<font color='#FAFAFA'>" + getString(R.string.app_name) + "</font>"))
    }

    fun initHunts()
    {
        addSampleHunts()
    }

    fun initRecyclerView()
    {
        huntListAdapter = HuntListAdapter(huntList,
                object: HuntListAdapter.onClickShare
                {
                    override fun invoke(hunt: Hunt)
                    {
                    }

                },
                object: HuntListAdapter.onClickJoin
                {
                    override fun invoke(hunt: Hunt)
                    {
                        startActivity(Intent(this@ChooseHuntActivity, MainActivity::class.java))
                    }

                })

        hunts_list_recycler_view.adapter = huntListAdapter
        hunts_list_recycler_view.layoutManager = LinearLayoutManager(this@ChooseHuntActivity)
    }

    fun initListeners()
    {
        fab.setOnClickListener { startActivity(Intent(this@ChooseHuntActivity, PlotMapActivity::class.java)) }
    }

    fun addSampleHunts()
    {
        huntList.add(Hunt(getString(R.string.hunt_sample_0), getString(R.string.hunt_description_sample_0), R.drawable.map_thumbnail_0, getString(R.string.hunt_prize_sample_0), 0, false))
        huntList.add(Hunt(getString(R.string.hunt_sample_1), getString(R.string.hunt_description_sample_1), R.drawable.map_thumbnail_1, getString(R.string.hunt_prize_sample_1), 0, false))
    }
}

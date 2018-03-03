package com.semaphores.techathlon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.semaphores.techathlon.Adapters.ClueListAdapter
import com.semaphores.techathlon.Models.Clue
import kotlinx.android.synthetic.main.activity_choose.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.clue.view.*
import kotlinx.android.synthetic.main.clue_image.view.*
import kotlinx.android.synthetic.main.clue_number.view.*
import kotlinx.android.synthetic.main.clues.*

class TreasureMapActivity : AppCompatActivity()
{
    val TAG = "TreasureMapActivity"
    var totalClues = 12
    var currentClue = -1
    var isNewClueReceived = false
    val clueList = mutableListOf<Clue>()
    lateinit var clueListAdapter: ClueListAdapter
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_map)

        initUI()
        initRecyclerView()
        initClues()
        initListener()
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
        /* Receive clue List here */
        totalClues = addSampleClues()
        unlockNextClue()
    }

    fun initListener()
    {
        clue_test_add.setOnClickListener { unlockNextClue() }
        fab.setOnClickListener { onNewClueReceived() }
    }

    fun addSampleClues() : Int
    {
        clueList.add(Clue(getString(R.string.clue_time_sample), getString(R.string.clue_sample)))
        clueList.add(Clue(getString(R.string.clue_time_sample), R.drawable.map_thumbnail_2))
        clueList.add(Clue(getString(R.string.clue_time_sample), getString(R.string.clue_sample)))
        clueList.add(Clue(getString(R.string.clue_time_sample), getString(R.string.clue_sample)))
        clueList.add(Clue(getString(R.string.clue_time_sample), getString(R.string.clue_sample)))
        clueList.add(Clue(getString(R.string.clue_time_sample), getString(R.string.clue_sample)))
        clueList.add(Clue(getString(R.string.clue_time_sample), getString(R.string.clue_sample)))
        clueList.add(Clue(getString(R.string.clue_time_sample), getString(R.string.clue_sample)))
        clueList.add(Clue(getString(R.string.clue_time_sample), getString(R.string.clue_sample)))

        return clueList.size
    }

    fun unlockNextClue()
    {
        if (currentClue < (clueList.size - 1))
        {
            currentClue += 1
            clueList[currentClue].isLocked = false
            //clueList[currentClue].time = getCurrentTime()
            // Only refresh the next clue view
            clueListAdapter.notifyItemChanged(currentClue)
            updateClueProgress()
        }
    }

    fun updateClueProgress()
    {
        clues_progress.text = String.format(getString(R.string.clues_progress), (currentClue + 1), totalClues)
    }

    fun onNewClueReceived()
    {
        unlockNextClue()
        val dialogBuilder = AlertDialog.Builder(this@TreasureMapActivity)
        var clueView : View = layoutInflater.inflate(R.layout.clue, null)
        with (clueList[currentClue])
        {

            if (isImage)
            {
                clueView = layoutInflater.inflate(R.layout.clue_image, null)
                clueView.clue_image.setImageResource(imageRes!!)
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
}


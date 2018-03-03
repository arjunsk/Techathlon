package com.semaphores.techathlon.Adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import com.semaphores.techathlon.R
import com.semaphores.techathlon.pojo.ClueDocument
import kotlinx.android.synthetic.main.clue.view.*
import kotlinx.android.synthetic.main.clue_image.view.*
import kotlinx.android.synthetic.main.clue_number.view.*

class ClueListAdapter(val clues: MutableList<ClueDocument>) : RecyclerView.Adapter<ClueListAdapter.ClueHolder>()
{
    val TAG = "ClueListAdapter"

    override fun onBindViewHolder(holder: ClueHolder?, position: Int)
    {
        holder?.bind(clues[position])
    }

    override fun getItemCount(): Int
    {
        return clues.size
    }

    override fun getItemViewType(position: Int): Int
    {
        if (clues[position].isLocked)
        {
            Log.i(TAG, "clue $position is locked")
            return 0
        }
        else
        {
            if (clues[position].isImage)
            {
                Log.i(TAG, "clue $position is image")
                return 2
            }
            else
            {
                Log.i(TAG, "clue $position is text")
                return 1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ClueHolder
    {
        when (viewType)
        {
            1 -> return ClueHolder(LayoutInflater.from(parent?.context).inflate(R.layout.clue, parent, false))
            2 -> return ClueHolder(LayoutInflater.from(parent?.context).inflate(R.layout.clue_image, parent, false))
            else -> return ClueHolder(LayoutInflater.from(parent?.context).inflate(R.layout.clue_locked, parent, false))
        }
    }

    class ClueHolder(item: View) : RecyclerView.ViewHolder(item)
    {
        val TAG = "ClueHolder"

        fun bind(clue: ClueDocument)
        {
            with(clue)
            {
                when (itemViewType)
                {
                    1 ->
                    {
                        itemView.clue_number.text = (adapterPosition + 1).toString()
                        itemView.clue_description.text = description
                    }

                    2 ->
                    {
                        itemView.clue_number.text = (adapterPosition + 1).toString()
                        itemView.clue_image.clue_image.setImageResource(R.drawable.clue_1)
                    }

                    else -> return
                }
            }
        }
    }
}

package com.semaphores.gofind.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.semaphores.techathlon.Models.Hunt
import com.semaphores.techathlon.R
import com.semaphores.techathlon.pojo.HuntDocument
import kotlinx.android.synthetic.main.hunt.view.*

class HuntListAdapter(val hunts: MutableList<HuntDocument>, val clickShare: HuntListAdapter.onClickShare, val clickJoin: HuntListAdapter.onClickJoin) : RecyclerView.Adapter<HuntListAdapter.HuntHolder>()
{
    override fun getItemCount(): Int
    {
        return hunts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HuntHolder
    {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.hunt, parent, false)
        return HuntHolder(view, clickShare, clickJoin)
    }

    override fun onBindViewHolder(holder: HuntHolder?, position: Int)
    {
        holder?.bind(hunts[position])
    }

    class HuntHolder(item: View, val clickShare: onClickShare, val clickJoin: onClickJoin) : RecyclerView.ViewHolder(item)
    {
        fun bind(hunt: HuntDocument)
        {
            with(hunt)
            {
                itemView.hunt_name.text = name
                itemView.hunt_prize.text = prize
                itemView.hunt_thumbnail.setImageResource(R.drawable.map_thumbnail_1)
                itemView.hunt_share_button.setOnClickListener { clickShare(this) }
                itemView.hunt_play_button.setOnClickListener { clickJoin(this) }
            }
        }
    }

    interface onClickShare
    {
        operator fun invoke(hunt: HuntDocument)
    }

    interface onClickJoin
    {
        operator fun invoke(hunt: HuntDocument)
    }
}

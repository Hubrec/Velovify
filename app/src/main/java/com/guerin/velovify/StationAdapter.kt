package com.guerin.velovify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guerin.velovify.databinding.VelovItemBinding
import com.guerin.velovify.objects.StationsItem

class StationAdapter : RecyclerView.Adapter<StationAdapter.ViewHolder>() {

    private var stationList = ArrayList<StationsItem>()

    fun setStationList(stationList: List<StationsItem>) {
        this.stationList = stationList as ArrayList<StationsItem>
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: VelovItemBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VelovItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load("https://www.ecully.fr/fileadmin/_processed_/4/f/csm_IMG_1481_9812aa1a70.jpg")
            .into(holder.binding.stationImage)
        val stationName = stationList[position].name.split(" - ")
        if (stationName.size > 1) {
            holder.binding.stationName.text = stationName[1]
        } else {
            holder.binding.stationName.text = stationList[position].name
        }
        holder.binding.stationBikes.text = "${stationList[position].totalStands.availabilities.bikes} vélos  /  "
        holder.binding.stationPlaces.text = "${stationList[position].totalStands.availabilities.stands} places"
    }

    override fun getItemCount(): Int {
        return stationList.size
    }
}

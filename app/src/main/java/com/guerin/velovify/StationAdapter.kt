package com.guerin.velovify

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.guerin.velovify.databinding.VelovItemBinding
import com.guerin.velovify.objects.StationsItem
import java.io.*

class StationAdapter : RecyclerView.Adapter<StationAdapter.ViewHolder>() {

    private var stationList = ArrayList<StationsItem>()

    fun setStationList(stationList: List<StationsItem>) {
        try {
            val file = File("/data/data/com.guerin.velovify/files/favorites.txt")
            if (file.exists()) {
                Log.i("StationAdapter", "File favorites.txt exists $file")

                FileInputStream(file).use {
                    val inputStream = ObjectInputStream(it)
                    val favoriteList = inputStream.readObject() as ArrayList<StationsItem>
                    for (favorite in favoriteList) {
                        for (station in stationList) {
                            if (favorite.name == station.name) {
                                station.favorite = true
                                Log.i("StationAdapter", "Passage dans station favorite ${station.name}")
                                break
                            }
                        }
                    }
                }
            } else {
                Log.i("StationAdapter", "File favorites.txt does not exist $file")
            }
        } catch (e: Exception) {
            Log.i("StationAdapter", "Error catch : $e")
        }
        this.stationList = stationList as ArrayList<StationsItem>
        Log.i("StationAdapter", "Station list size : ${stationList.size}")
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: VelovItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val stationName = binding.stationName.text
                Toast.makeText(binding.root.context, "Station $stationName", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
        if (stationList[position].totalStands.availabilities.bikes == 0) {
            holder.binding.stationImage.setImageResource(R.drawable.vide)
        } else if (stationList[position].totalStands.availabilities.stands == 0) {
            holder.binding.stationImage.setImageResource(R.drawable.plein)
        } else {
            holder.binding.stationImage.setImageResource(R.drawable.valid)
        }

        val stationName = stationList[position].name.split(" - ")
        if (stationName.size > 1) {
            if (stationName[1].length > 30) {
                holder.binding.stationName.text = stationName[1].substring(0, 25) + " ..."
            } else {
                holder.binding.stationName.text = stationName[1]
            }
        } else {

            if (stationList[position].name.length > 30) {
                holder.binding.stationName.text = stationList[position].name.substring(0, 25) + " ..."
            } else {
                holder.binding.stationName.text = stationList[position].name
            }
        }
        holder.binding.stationBikes.text = "${stationList[position].totalStands.availabilities.bikes} vélos  /  "
        holder.binding.stationPlaces.text = "${stationList[position].totalStands.availabilities.stands} places"

        if (stationList[position].favorite) {
            holder.binding.buttonAddToFav.background = holder.binding.root.context.getDrawable(R.drawable.favorite_filled)
        } else {
            holder.binding.buttonAddToFav.background = holder.binding.root.context.getDrawable(R.drawable.favorite_empty)
        }

        holder.binding.buttonAddToFav.setOnClickListener {
            addToFavorite(holder.binding.stationName.text)
        }

        holder.binding.root.setOnLongClickListener() {
            addToFavorite(holder.binding.stationName.text)
            true
        }
    }

    override fun getItemCount(): Int {
        return stationList.size
    }

    fun addToFavorite(stationName: CharSequence) {
        var name = stationName
        if ("..." in stationName) {
            name = stationName.substring(0, stationName.length - 4)
        }
        val station = stationList.find { it.name.contains(name) }
        station?.favorite = !station?.favorite!!

        val file = File("/data/data/com.guerin.velovify/files/favorites.txt")
        var listeTemp = arrayListOf<StationsItem>()
        FileOutputStream(file).use {
            val outputStream = ObjectOutputStream(it)
            for (station in stationList) {
                if (station.favorite) {
                    listeTemp.add(station)
                }
            }
            outputStream.writeObject(listeTemp)
        }
        notifyDataSetChanged()
    }
}
package com.example.inventoryapp.rvadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R
import com.example.inventoryapp.database.Location


class LocationAdapter(val fn: (holder: ViewHolder, item: Location) -> Unit ={ _, _ ->})
    : ListAdapter<Location, LocationAdapter.ViewHolder>(DiffCallback) {
    companion object DiffCallback: DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location) = oldItem.locationId == newItem.locationId

        override fun areContentsTheSame(oldItem: Location, newItem: Location) = oldItem == newItem
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
       val root = view
        val btnLocation: Button = root.findViewById(R.id.btnLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.btnLocation.text = item.placeTag

        fn(holder, item)
    }
}
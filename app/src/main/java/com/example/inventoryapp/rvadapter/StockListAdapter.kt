package com.example.inventoryapp.rvadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.inventoryapp.R
import com.example.inventoryapp.database.Product
import com.example.inventoryapp.toBitmap

class StockListAdapter(val fn: (holder: ViewHolder, item: Product) -> Unit ={ _, _ ->})
    : ListAdapter<Product, StockListAdapter.ViewHolder>(DiffCallback) {
    companion object DiffCallback: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.barcode == newItem.barcode

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val root = view
        val pImage: ImageView = root.findViewById(R.id.pImg)
        val pName: TextView = root.findViewById((R.id.pName))
        val pQty: TextView = root.findViewById(R.id.pQty)
        val btnIncStock: TextView = root.findViewById(R.id.btnIncStock)
        val btnDecStock: TextView = root.findViewById(R.id.btnDecStock)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.pImage.load(item.photo.toBitmap()){placeholder((R.drawable.ic_image_24))}
        holder.pName.text = item.name
        holder.pQty.text = item.stockQty.toString()

        fn(holder, item)
    }
}
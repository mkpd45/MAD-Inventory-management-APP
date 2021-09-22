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
import com.example.inventoryapp.R
import com.example.inventoryapp.database.Product
import com.example.inventoryapp.toBitmap

class ProductAdapter(val fn: (holder: ViewHolder, item: Product) -> Unit ={ _, _ ->})
    : ListAdapter<Product, ProductAdapter.ViewHolder>(DiffCallback) {
    companion object DiffCallback: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.barcode == newItem.barcode

        override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val root = view
        val imgPhoto: ImageView = root.findViewById(R.id.img)
        val txtTitle: TextView = root.findViewById(R.id.title)
        val txtSec: TextView = root.findViewById(R.id.txtSec)
        val txtSupp: TextView = root.findViewById(R.id.txtSupp)
        val btnEditP: TextView = root.findViewById(R.id.btnEditP)
        val btnDeleteP: TextView = root.findViewById(R.id.btnDeleteP)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
//        holder.imgPhoto.load(item.photo){placeholder((R.drawable.ic_error))}

        holder.txtTitle.text = item.name
        holder.txtSec.text = item.barcode
        holder.txtSupp.text = "RM%.2f".format(item.price.toString().toDoubleOrNull()?:0)
        holder.imgPhoto.setImageBitmap(item.photo.toBitmap())

        fn(holder, item)
    }
}
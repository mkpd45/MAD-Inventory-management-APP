package com.example.inventoryapp.rvadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R
import com.example.inventoryapp.database.Supplier
import com.example.inventoryapp.toBitmap

class SupplierAdapter  (
    val fn: (ViewHolder, Supplier) -> Unit = { _, _ -> }
) : ListAdapter<Supplier, SupplierAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Supplier>() {
        override fun areItemsTheSame(a: Supplier, b: Supplier)    = a.id == b.id
        override fun areContentsTheSame(a: Supplier, b: Supplier) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto2 : ImageView = view.findViewById(R.id.imgPhoto2)
        val txtId2    : TextView = view.findViewById(R.id.txtId2)
        val txtName2  : TextView = view.findViewById(R.id.txtName2)
        val txtEmail2  : TextView = view.findViewById(R.id.txtEmail2)
        val txtPhone2 : TextView = view.findViewById(R.id.txtPhone2)
        val btnDelete2: Button = view.findViewById(R.id.btnDelete2)
        val btnEdit2: Button = view.findViewById(R.id.btnEdit2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list2_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val supplier = getItem(position)

        holder.txtId2.text   = supplier.id
        holder.txtName2.text = supplier.name
        holder.txtEmail2.text = supplier.email
        holder.txtPhone2.text  = supplier.phone.toString()

        // TODO: Photo (blob to bitmap)
        holder.imgPhoto2.setImageBitmap(supplier.photo.toBitmap())

        fn(holder, supplier)
    }
}
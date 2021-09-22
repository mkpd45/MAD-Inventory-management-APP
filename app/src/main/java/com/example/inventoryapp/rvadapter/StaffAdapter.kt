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
import com.example.inventoryapp.database.Staff
import com.example.inventoryapp.toBitmap

class StaffAdapter (
    val fn: (ViewHolder, Staff) -> Unit = { _, _ -> }
) : ListAdapter<Staff, StaffAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Staff>() {
        override fun areItemsTheSame(a: Staff, b: Staff)    = a.id == b.id
        override fun areContentsTheSame(a: Staff, b: Staff) = a == b
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val root = view
        val imgPhoto : ImageView = view.findViewById(R.id.imgPhoto)
        val txtId    : TextView = view.findViewById(R.id.txtId)
        val txtName  : TextView = view.findViewById(R.id.txtName)
        val txtEmail  : TextView = view.findViewById(R.id.txtEmail)
        val txtPhone : TextView = view.findViewById(R.id.txtPhone)
        val txtRole : TextView = view.findViewById(R.id.txtRole)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val staff = getItem(position)

        holder.txtId.text   = staff.id
        holder.txtName.text = staff.name
        holder.txtEmail.text = staff.email
        holder.txtPhone.text  = staff.phone.toString()
        holder.txtRole.text = staff.role

        // TODO: Photo (blob to bitmap)
        holder.imgPhoto.setImageBitmap(staff.photo.toBitmap())

        fn(holder, staff)
    }

}
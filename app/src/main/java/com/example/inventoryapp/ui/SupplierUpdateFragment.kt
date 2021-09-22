package com.example.inventoryapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.*
import com.example.inventoryapp.database.Supplier
import com.example.inventoryapp.databinding.FragmentSupplierUpdateBinding
import com.example.inventoryapp.viewmodel.SupplierViewModel
import java.text.SimpleDateFormat
import java.util.*

class SupplierUpdateFragment : Fragment() {

    private lateinit var binding: FragmentSupplierUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id") ?: "" }
    private val formatter = SimpleDateFormat("dd MMMM yyyy '-' hh:mm:ss a", Locale.getDefault())

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupplierUpdateBinding.inflate(inflater,container,false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }
        binding.btnMap.setOnClickListener { openMap() }
        binding.btnCall2.setOnClickListener{ openPhone() }

        return binding.root
    }

    private fun openPhone() {
        val mobileNumber : String = binding.edtPhone.text.toString()
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        intent.data = Uri.parse("tel: +60$mobileNumber")
        startActivity(intent)
    }

    private fun openMap() {
        val lati : String = binding.edtLati.text.toString()
        val long : String = binding.edtLong.text.toString()
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse("geo:$lati,$long?q=")
        startActivity(intent)
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        // TODO: Load data
        val sup = vm.get(id)

        if (sup == null) {
            nav.navigateUp()
            return
        }

        load(sup)
    }

    private fun load(sup: Supplier) {
        binding.txtId.text = sup.id
        binding.edtName.setText(sup.name)
        binding.edtEmail.setText(sup.email)
        binding.edtLati.setText(sup.location.latitude.toString())
        binding.edtLong.setText(sup.location.longitude.toString())
        binding.edtAddress.setText(sup.address)
        binding.edtPhone.setText(sup.phone.toString())

        // TODO: Load photo and date
        binding.imgPhoto.setImageBitmap(sup.photo.toBitmap())
        binding.txtDate.text = formatter.format(sup.date)

        binding.edtName.requestFocus()
    }

    private fun submit() {
        // TODO: Update (set)
        val sup = Supplier(
            id    = id,
            name  = binding.edtName.text.toString().trim(),
            email  = binding.edtEmail.text.toString().trim(),
            lati = binding.edtLati.text.toString().toDoubleOrNull() ?: 0.0,
            longi = binding.edtLong.text.toString().toDoubleOrNull() ?: 0.0,
            address  = binding.edtAddress.text.toString().trim(),
            phone   = binding.edtPhone.text.toString().toLongOrNull() ?: 0,
            photo = binding.imgPhoto.cropToBlob(300, 300)
        )

        val err = vm.validate(sup, false)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(sup)
        nav.navigateUp()
    }

    private fun delete() {
        // TODO: Delete
        vm.delete(id)
        nav.navigateUp()
    }

}
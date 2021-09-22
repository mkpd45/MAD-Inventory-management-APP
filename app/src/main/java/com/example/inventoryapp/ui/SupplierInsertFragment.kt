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
import com.example.inventoryapp.viewmodel.SupplierViewModel
import com.example.inventoryapp.cropToBlob
import com.example.inventoryapp.database.Supplier
import com.example.inventoryapp.databinding.FragmentSupplierInsertBinding
import com.example.inventoryapp.errorDialog


class SupplierInsertFragment : Fragment() {

    private lateinit var binding: FragmentSupplierInsertBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupplierInsertBinding.inflate(inflater,container,false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnMap.setOnClickListener { openMap() }

        return binding.root
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
        binding.edtId.text.clear()
        binding.edtName.text.clear()
        binding.edtEmail.text.clear()
        binding.edtPhone.text.clear()
        binding.edtLati.text.clear()
        binding.edtLong.text.clear()
        binding.edtAddress.text.clear()
        binding.imgPhoto.setImageDrawable(null)
        binding.edtId.requestFocus()
    }

    private fun submit() {
        // TODO: Insert (set)
        val sup = Supplier(
            id    = binding.edtId.text.toString().trim().uppercase(),
            name  = binding.edtName.text.toString().trim(),
            email  = binding.edtEmail.text.toString().trim(),
            phone   = binding.edtPhone.text.toString().toLongOrNull() ?: 0,
            lati = binding.edtLati.text.toString().toDoubleOrNull() ?: 0.0,
            longi = binding.edtLong.text.toString().toDoubleOrNull() ?: 0.0,
            address  = binding.edtAddress.text.toString().trim(),
            photo = binding.imgPhoto.cropToBlob(300, 300),
        )

        val err = vm.validate(sup)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(sup)
        nav.navigateUp()
    }
}
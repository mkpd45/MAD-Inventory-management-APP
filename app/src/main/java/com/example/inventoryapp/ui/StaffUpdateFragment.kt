package com.example.inventoryapp.ui

import com.example.inventoryapp.viewmodel.StaffViewModel
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
import com.example.inventoryapp.database.Staff
import com.example.inventoryapp.databinding.FragmentStaffUpdateBinding
import java.text.SimpleDateFormat
import java.util.*


class StaffUpdateFragment : Fragment() {

    private lateinit var binding: FragmentStaffUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: StaffViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id") ?: "" }
    private val formatter = SimpleDateFormat("dd MMMM yyyy '-' hh:mm:ss a", Locale.getDefault())

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStaffUpdateBinding.inflate(inflater,container,false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }
        binding.btnCall.setOnClickListener{ openPhone() }

        return binding.root
    }

    private fun openPhone() {
        val mobileNumber : String = binding.edtPhone.text.toString()
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        intent.data = Uri.parse("tel: +60$mobileNumber")
        startActivity(intent)
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        // TODO: Load data
        val s = vm.get(id)

        if (s == null) {
            nav.navigateUp()
            return
        }

        load(s)
    }

    private fun load(s: Staff) {
        binding.txtId.text = s.id
        binding.edtName.setText(s.name)
        binding.edtEmail.setText(s.email)
        binding.edtPassword.setText(s.password)
        binding.edtPhone.setText(s.phone.toString())
        binding.txtRole.text = s.role

        // TODO: Load photo and date
        binding.imgPhoto.setImageBitmap(s.photo.toBitmap())
        binding.txtDate.text = formatter.format(s.date)

        binding.edtName.requestFocus()
    }

    private fun submit() {
        // TODO: Update (set)
        val s = Staff(
            id    = id,
            name  = binding.edtName.text.toString().trim(),
            email  = binding.edtEmail.text.toString().trim(),
            password  = binding.edtPassword.text.toString().trim(),
            phone   = binding.edtPhone.text.toString().toLongOrNull() ?: 0,
            role = binding.txtRole.text.toString(),
            photo = binding.imgPhoto.cropToBlob(300, 300)
        )

        val err = vm.validate(s, false)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(s)
        nav.navigateUp()
    }

    private fun delete() {
        // TODO: Delete
        vm.delete(id)
        nav.navigateUp()
    }

}
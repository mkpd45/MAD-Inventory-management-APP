package com.example.inventoryapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.viewmodel.StaffViewModel
import com.example.inventoryapp.cropToBlob
import com.example.inventoryapp.database.Staff
import com.example.inventoryapp.databinding.FragmentStaffInsertBinding
import com.example.inventoryapp.errorDialog


class StaffInsertFragment : Fragment() {

    private lateinit var binding: FragmentStaffInsertBinding
    private val nav by lazy { findNavController() }
    private val vm: StaffViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStaffInsertBinding.inflate(inflater,container,false)

        reset()
        binding.imgPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnSubmit.setOnClickListener { submit() }

        return binding.root
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
        binding.edtPassword.text.clear()
        binding.edtPhone.text.clear()
        binding.spinner.setSelection(0)
        binding.imgPhoto.setImageDrawable(null)
        binding.edtId.requestFocus()
    }

    private fun submit() {
        // TODO: Insert (set)
        val s = Staff(
            id    = binding.edtId.text.toString().trim().uppercase(),
            name  = binding.edtName.text.toString().trim(),
            email  = binding.edtEmail.text.toString().trim(),
            password  = binding.edtPassword.text.toString().trim(),
            phone   = binding.edtPhone.text.toString().toLongOrNull() ?: 0,
            role = binding.spinner.selectedItem.toString(),
            photo = binding.imgPhoto.cropToBlob(300, 300),
        )

        val err = vm.validate(s)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(s)
        nav.navigateUp()
    }

}
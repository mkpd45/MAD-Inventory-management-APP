package com.example.inventoryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.inventoryapp.R
import com.example.inventoryapp.viewmodel.StaffViewModel
import com.example.inventoryapp.databinding.FragmentStaffListBinding
import com.example.inventoryapp.hideKeyboard
import com.example.inventoryapp.rvadapter.StaffAdapter

class StaffListFragment : Fragment() {

    private lateinit var binding: FragmentStaffListBinding
    private val nav by lazy { findNavController() }
    private val vm: StaffViewModel by activityViewModels()

    private lateinit var adapter: StaffAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStaffListBinding.inflate(inflater,container,false)

        // TODO: Default search and sort
        vm.search("")
        sort("id")

        binding.fabInsert.setOnClickListener { nav.navigate(R.id.staffInsertFragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

        adapter = StaffAdapter() { holder, staff ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.staffUpdateFragment, bundleOf("id" to staff.id))
            }
            // Delete button click
            holder.btnDelete.setOnClickListener { delete(staff.id) }
            //Edit button click
            holder.btnEdit.setOnClickListener{
                nav.navigate(R.id.staffUpdateFragment, bundleOf("id" to staff.id))
            }
        }
        binding.rv1.adapter = adapter
        binding.rv1.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // TODO: Load data
        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtCount.text = "${list.size} staff(s)"
        }

        binding.sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                // TODO: Search by [name]
                vm.search(name)
                hideKeyboard()
                return true
            }
        })

        binding.btnId.setOnClickListener { sort("id") }
        binding.btnName.setOnClickListener { sort("name") }

        return binding.root
    }

    private fun deleteAll() {
        // TODO: Delete all
        vm.deleteAll()
    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.delete(id)
    }

    private fun sort(field: String) {
        // TODO: Sort by [field]
        val reverse = vm.sort(field)

        // TODO: Remove icon
        binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        // TODO: Set icon
        val res = if (reverse) R.drawable.ic_down else R.drawable.ic_up

        when (field) {
            "id"    -> binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "name"  -> binding.btnName.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
        }
    }

}
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
import com.example.inventoryapp.viewmodel.SupplierViewModel
import com.example.inventoryapp.databinding.FragmentSupplierListBinding
import com.example.inventoryapp.hideKeyboard
import com.example.inventoryapp.rvadapter.SupplierAdapter

class SupplierListFragment : Fragment() {

    private lateinit var binding: FragmentSupplierListBinding
    private val nav by lazy { findNavController() }
    private val vm: SupplierViewModel by activityViewModels()

    private lateinit var adapter: SupplierAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupplierListBinding.inflate(inflater,container,false)

        // TODO: Default search and sort
        vm.search("")
        sort("id")

        binding.fabInsert.setOnClickListener { nav.navigate(R.id.supplierInsertFragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

        adapter = SupplierAdapter() { holder, supplier ->
            // Item click
            holder.root.setOnClickListener {
                nav.navigate(R.id.supplierUpdateFragment, bundleOf("id" to supplier.id))
            }
            // Delete button click
            holder.btnDelete2.setOnClickListener { delete(supplier.id) }
            //Edit button click
            holder.btnEdit2.setOnClickListener{
                nav.navigate(R.id.supplierUpdateFragment, bundleOf("id" to supplier.id))
            }
        }
        binding.rv2.adapter = adapter
        binding.rv2.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        vm.getAll().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.txtCount.text = "${list.size} supplier(s)"
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
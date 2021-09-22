package com.example.inventoryapp.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.klModel.ProductViewModel
import com.example.inventoryapp.R
import com.example.inventoryapp.databinding.FragmentProductsBinding
import com.example.inventoryapp.rvadapter.ProductAdapter
import com.google.android.material.tabs.TabLayout


class ProductsFragment : Fragment() {


    private val pVM: ProductViewModel by activityViewModels()
    private lateinit var binding: FragmentProductsBinding
    private val nav by lazy {findNavController()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)

//        binding.cardView.setOnLongClickListener{
//            binding.cardView.isChecked = !binding.cardView.isChecked
//            true
//        }

        val adapter = ProductAdapter { holder, product ->
            val args = bundleOf("barcode" to product.barcode,"name" to product.name, "price" to product.price
                                , "category" to product.category, "supplier" to product.supplier )

            holder.imgPhoto.setOnClickListener{Toast.makeText(context,product.name,Toast.LENGTH_SHORT).show()}

            holder.btnDeleteP.setOnClickListener { deleteConfirm(product.barcode,product.name) }

            holder.btnEditP.setOnClickListener { nav.navigate(R.id.action_productsFragment_to_maintainProductFragment, args) }
        }
        binding.rv.adapter = adapter

        pVM.getResult().observe(viewLifecycleOwner) { products -> adapter.submitList(products) }

        binding.fabAddP.setOnClickListener { nav.navigate(R.id.action_productsFragment_to_addProductFragment) }

        binding.productTab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                pVM.tabFilter(tab?.text.toString())
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

        return binding.root
    }

    private fun deleteConfirm(barcode: String, name: String){
        AlertDialog.Builder(context)
            .setIcon(R.drawable.ic_warning_24)
            .setTitle("Delete product")
            .setMessage("Confirm to delete this product?")
            .setPositiveButton("Yes") { _, _ ->
                pVM.delete(barcode)
                Toast.makeText(requireContext(), "$name deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

}
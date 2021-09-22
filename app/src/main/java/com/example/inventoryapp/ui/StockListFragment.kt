package com.example.inventoryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.klModel.ProductViewModel
import com.example.inventoryapp.R
import com.example.inventoryapp.databinding.FragmentStockListBinding
import com.example.inventoryapp.rvadapter.StockListAdapter


class StockListFragment : Fragment() {
    private lateinit var binding: FragmentStockListBinding
    private val pVM:ProductViewModel by activityViewModels()
    private val nav by lazy{findNavController()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var opString: String
       binding = FragmentStockListBinding.inflate(inflater, container, false)
        var args:Bundle

        val adapter = StockListAdapter(){holder, product ->

            holder.btnIncStock.setOnClickListener {
                opString = "increase"
               args = bundleOf("barcode" to product.barcode,"name" to product.name, "stockQuantity" to product.stockQty
                , "operation" to opString )

                nav.navigate(R.id.action_stockListFragment_to_stockActionFragment, args)
            }

            holder.btnDecStock.setOnClickListener {
                opString = "decrease"
                args = bundleOf("barcode" to product.barcode,"name" to product.name, "stockQuantity" to product.stockQty
                    , "operation" to opString )
                nav.navigate(R.id.action_stockListFragment_to_stockActionFragment, args)
            }
        }
        binding.list.adapter = adapter

        pVM.getResult().observe(viewLifecycleOwner) { products -> adapter.submitList(products) }

        return binding.root
    }

}
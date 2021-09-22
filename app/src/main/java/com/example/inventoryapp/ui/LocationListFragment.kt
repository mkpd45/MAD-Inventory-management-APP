package com.example.inventoryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.R
import com.example.inventoryapp.databinding.FragmentLocationListBinding
import com.example.inventoryapp.klModel.LocationViewModel
import com.example.inventoryapp.rvadapter.LocationAdapter


class LocationListFragment : Fragment() {
  private lateinit var binding: FragmentLocationListBinding
  private val lVM: LocationViewModel by activityViewModels()
  private val nav by lazy{findNavController()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this
        binding = FragmentLocationListBinding.inflate(inflater, container, false)
        
        val adapter=LocationAdapter{ holder, item ->
            val args = bundleOf("placeTag" to item.placeTag, "rackSlot" to item.rackSlot)
            holder.btnLocation.setOnClickListener { nav.navigate(R.id.locationDetailFragment,args) }
        }

        binding.rvLlist.adapter = adapter

        lVM.getResult().observe(viewLifecycleOwner) { locations -> adapter.submitList(locations) }
        
        return binding.root
    }


    }

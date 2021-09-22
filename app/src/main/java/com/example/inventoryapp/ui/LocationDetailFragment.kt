package com.example.inventoryapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.databinding.FragmentLocationDetailBinding



class LocationDetailFragment : Fragment() {
    private lateinit var binding: FragmentLocationDetailBinding
    private val placeTag by lazy { requireArguments().getString("placeTag", "")}
    private val rackSlot by lazy { requireArguments().getInt("rackSlot", 0)}

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentLocationDetailBinding.inflate(inflater, container, false)
        binding.tag.text = placeTag
        container?.removeView(binding.rackToAdd.parent as View)

        for(i in 1 until  rackSlot) {
            binding.rackToAdd.text = "$placeTag$i"
            binding.rackSlot.addView(binding.rackToAdd)
        }

        return binding.root
    }

}
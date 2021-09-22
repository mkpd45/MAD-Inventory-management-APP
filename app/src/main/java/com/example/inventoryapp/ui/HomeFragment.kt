package com.example.inventoryapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.R
import com.example.inventoryapp.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val nav by lazy {findNavController()}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

//            val sharedPreferences:SharedPreferences = requireActivity().getSharedPreferences("loginSession",
//            AppCompatActivity.MODE_PRIVATE)
//        checkLoginSession(sharedPreferences) //Identify whether the user is logged out or not

        binding.btnProd.setOnClickListener{nav.navigate(R.id.action_homeFragment_to_productsFragment)}
        //binding.button6.isVisible = false
        binding.btnStaff.setOnClickListener{nav.navigate(R.id.staffListFragment)}
        binding.btnSupplier.setOnClickListener{nav.navigate(R.id.supplierListFragment)}
        binding.btnStockManage.setOnClickListener { nav.navigate(R.id.action_homeFragment_to_stockListFragment) }
        binding.btnToLocation.setOnClickListener { nav.navigate(R.id.action_homeFragment_to_locationListFragment) }
        // binding.btnLogout.setOnClickListener{logout(sharedPreferences)}

        return binding.root
    }

//    private fun logout(sharedPreferences: SharedPreferences) {
//        val editor:SharedPreferences.Editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply()
//        Toast.makeText(requireActivity(),"You are logged out, Thank you~",Toast.LENGTH_SHORT).show()
//        nav.navigate(R.id.loginFragment)
//    }

//    private fun checkLoginSession(sharedPreferences: SharedPreferences) {
//        if(sharedPreferences.getString("username","") != "" &&
//            sharedPreferences.getString("password","") != "")
//            Toast.makeText(context,"It works!", Toast.LENGTH_SHORT).show()
//        else{
//            nav.navigate(R.id.loginFragment)
//        }
//    }
}
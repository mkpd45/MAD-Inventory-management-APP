package com.example.inventoryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.inventoryapp.database.Staff
import com.example.inventoryapp.databinding.ActivityMainBinding
import com.example.inventoryapp.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private val nav by lazy {
        supportFragmentManager.findFragmentById(R.id.host)!!.findNavController()
    }
    private val log: LoginViewModel by viewModels()

    // AppBarConfiguration
    private lateinit var abc: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // AppBarConfiguration
        abc = AppBarConfiguration(
            setOf(
                // Top-level pages
                R.id.homeFragment,
                R.id.staffInsertFragment,
                R.id.productsFragment,
                R.id.supplierListFragment,
                R.id.staffListFragment,
            ),
            binding.drawerLayout
        )

        // Setup action bar and navigation view
        setupActionBarWithNavController(nav, abc)
        binding.navView.setupWithNavController(nav)

        // TODO: Observe login status
        log.getUserLiveData().observe(this) { staff ->
            // TODO: Clear menu + remove header
            binding.navView.menu.clear()
            val h = binding.navView.getHeaderView(0)
            binding.navView.removeHeaderView(h)

            // TODO: Inflate menu + header
            if (staff == null) {
                binding.navView.inflateMenu(R.menu.login_drawer)
                binding.navView.inflateHeaderView(R.layout.header)
                nav.popBackStack(R.id.homeFragment, false)
                nav.navigateUp()
            }
            else {
                if(staff.role == "Manager"){
                    binding.navView.inflateMenu(R.menu.manager_drawer)
                    binding.navView.inflateHeaderView(R.layout.header_login)
                    setHeader(staff)
                }else if(staff.role == "Staff"){
                    binding.navView.inflateMenu(R.menu.staff_drawer)
                    binding.navView.inflateHeaderView(R.layout.header_login)
                    setHeader(staff)
                }

            }

            // TODO: Handle Logout menu item
            binding.navView.menu.findItem(R.id.logout)?.setOnMenuItemClickListener { logout() }
        }

        // TODO: Auto login
        lifecycleScope.launch { log.loginFromPreferences(this@MainActivity) }

    }

    private fun setHeader(staff: Staff) {
        val h = binding.navView.getHeaderView(0)

        val imgPhoto: ImageView = h.findViewById(R.id.imgPhoto)
        val txtName : TextView = h.findViewById(R.id.txtName)
        val txtEmail: TextView = h.findViewById(R.id.txtEmail)
        val profileView : View = h.findViewById(R.id.profileView)

        imgPhoto.setImageBitmap(staff.photo?.toBitmap())
        txtName.text  = staff.name
        txtEmail.text = staff.email
        profileView.setOnClickListener{nav.navigate(R.id.staffUpdateFragment, bundleOf("id" to staff.id))}
    }

    private fun logout(): Boolean {
        // TODO(4): Logout
        log.logout(this)
        nav.popBackStack(R.id.homeFragment, false)
        nav.navigateUp()

        binding.drawerLayout.close()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp(abc) || super.onSupportNavigateUp()
    }

}
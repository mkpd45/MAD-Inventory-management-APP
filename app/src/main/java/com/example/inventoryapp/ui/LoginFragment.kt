package com.example.inventoryapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.inventoryapp.*
import com.example.inventoryapp.databinding.FragmentLoginBinding
import com.example.inventoryapp.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val nav by lazy { findNavController() }
    private val log: LoginViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.btnLogin.setOnClickListener{login()}
        binding.btnForgot.setOnClickListener{
            val intent = Intent(context, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun login() {
        hideKeyboard()

        val ctx = requireContext()
        val name = binding.editName.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()
        val remember = binding.chkRemember.isChecked

        lifecycleScope.launch {
            val success = log.login(ctx, name, password, remember)
            if (success) {
                nav.popBackStack(R.id.homeFragment, false)
                nav.navigateUp()
            } else {
                errorDialog("Invalid login credentials.")
            }

        }
    }

}
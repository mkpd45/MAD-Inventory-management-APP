package com.example.inventoryapp

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.forgot_password.*

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)

        btnClose.setOnClickListener{
            finish()
        }

        btnResetPass.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            if (editEmail.text.toString().isNullOrEmpty())
                //textViewResponse.text = "Email Address is not provided"
            Toast.makeText(this, "Email Address is not provided", Toast.LENGTH_SHORT).show()
            else {
                auth.sendPasswordResetEmail(
                    editEmail.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            //textViewResponse.text = "Reset Password Link is mailed"
                            Toast.makeText(this, "Reset Password Link is mailed", Toast.LENGTH_SHORT).show()
                        } else
                            //textViewResponse.text = "Password Reset mail could not be sent"
                        Toast.makeText(this, "Password Reset mail could not be sent", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}

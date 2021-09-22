package com.example.inventoryapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.inventoryapp.database.STAFFS
import com.example.inventoryapp.database.Staff
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val staffLiveData = MutableLiveData<Staff>()
    private var listener: ListenerRegistration? = null

    // Remove snapshot listener when view model is destroyed
    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    // Return observable live data
    fun getUserLiveData(): LiveData<Staff> {
        return staffLiveData
    }

//    // Return user from live data
//    fun getUser(): Staff? {
//        return staffLiveData.value
//    }

    // TODO: Login
    suspend fun login(ctx: Context, name: String, password: String, remember: Boolean = false): Boolean {
        // TODO: Get the user record with matching email + password
        //           Return false is no matching found
        val staff = STAFFS
            //.whereEqualTo(FieldPath.documentId(), "XXX")
            .whereEqualTo("name", name)
            .whereEqualTo("password", password)
            .get()
            .await()
            .toObjects<Staff>()
            .firstOrNull() ?: return false

        // TODO: Setup snapshot listener
        //           Update live data -> staff
        listener?.remove()
        listener = STAFFS.document(staff.id).addSnapshotListener { doc, _ ->
            staffLiveData.value = doc?.toObject()
        }

        // TODO: Handle remember-me -> add shared preferences
        if (remember) {
            getPreferences(ctx)
                .edit()
                .putString("name", name)
                .putString("password", password)
                .apply()
        }

        return true
    }

    // TODO: Logout
    fun logout(ctx: Context) {
        // TODO(2A): Remove snapshot listener
        //           Update live data -> null
        listener?.remove()
        staffLiveData.value = null

        // TODO: Handle remember-me -> clear shared preferences
        getPreferences(ctx).edit().clear().apply()

        // getPreferences(ctx).edit().remove("email").remove("password").apply()

        // ctx.deleteSharedPreferences("AUTH")
    }

    // TODO: Get shared preferences
    private fun getPreferences(ctx: Context): SharedPreferences {
        // return ctx.getSharedPreferences("AUTH", Context.MODE_PRIVATE)

        return EncryptedSharedPreferences.create(
            "AUTH",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            ctx,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // TODO: Auto login from shared preferences
    suspend fun loginFromPreferences(ctx: Context) {
        val pref = getPreferences(ctx)
        val name    = pref.getString("name", null)
        val password = pref.getString("password", null)

        if (name != null && password != null) {
            login(ctx, name, password)
        }
    }
}
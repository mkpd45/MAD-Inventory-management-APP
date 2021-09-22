package com.example.inventoryapp.viewmodel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inventoryapp.database.SUPPLIERS
import com.example.inventoryapp.database.Supplier
import com.google.firebase.firestore.ktx.toObjects

class SupplierViewModel : ViewModel() {
    // TODO: Initialization
    private var supp = listOf<Supplier>()
    private val result = MutableLiveData<List<Supplier>>()

    private var name = ""       // Search
    private var field = ""      // Sort
    private var reverse = false // Sort

    init {
        SUPPLIERS.addSnapshotListener { snap, _ ->
            if (snap == null) return@addSnapshotListener

            supp = snap.toObjects<Supplier>()
            updateResult()
        }
    }

    fun get(id: String): Supplier? {
        return result.value?.find { sup -> sup.id == id }
    }

    fun getSuppliers(): List<Supplier>?{
        return result.value
    }

    fun getAll() = result

    fun delete(id: String) {
        // TODO
        SUPPLIERS.document(id).delete()
    }

    fun deleteAll() {
        // TODO
        result.value?.forEach { sup -> delete(sup.id) }
    }

    fun set(sup: Supplier) {
        // TODO
        SUPPLIERS.document(sup.id).set(sup)
    }

    private fun idExists(id: String): Boolean {
        return result.value?.any { sup -> sup.id == id } ?: false
    }

    private fun nameExists(name: String): Boolean {
        return result.value?.any { sup -> sup.name == name } ?: false
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validate(s: Supplier, insert: Boolean = true): String {
        val regexId = Regex("""^[0-9A-Z]{5}$""")
        val regexName = Regex("""^[A-Za-z]{50}$""")
        var e = ""

        if (insert) {
            e += if (s.id == "") "- Id is required.\n"
            else if (!s.id.matches(regexId)) "- Id format is invalid.\n"
            else if (idExists(s.id)) "- Id is duplicated.\n"
            else ""
        }

        if (insert) {
            e += if (s.name == "") "- Name is required.\n"
            else if (s.name.matches(regexName)) "- Name format is invalid.\n"
            else if (s.name.length < 3) "- Name is too short.\n"
            else if (nameExists(s.name)) "- Name is duplicated.\n"
            else ""
        }

        if (insert) {
            e += if (s.email == "") "- Email is required.\n"
            else if (!isValidEmail(s.email)) "- Email format is invalid.\n"
            else ""
        }

        if (insert) {
            e += if (s.phone == 0L) "- Phone is required.\n"
            else if (s.phone.toString().length < 10L) "- Invalid phone Number.\n"
            else ""
        }

        e += if (s.lati == 0.0) "- Latitude is required.\n"
        else ""

        e += if (s.longi == 0.0) "- Longitude is required.\n"
        else ""

        e += if (s.address == "") "- Address is required.\n"
        else ""

        e += if (s.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }

    // TODO(15): Get search + filter + sort result
    private fun updateResult() {
        var list = supp

        // TODO(23): Search + filter
        list = list.filter { s ->
            s.name.contains(name, true)
        }

        // TODO(24): Sort
        list = when (field) {
            "id"    -> list.sortedBy { s -> s.id }
            "name"  -> list.sortedBy { s -> s.name }
            else    -> list
        }

        if (reverse) list = list.reversed()

        result.value = list
    }

    // TODO: Set [name]
    fun search(name: String) {
        this.name = name
        updateResult()
    }

    // TODO: Set [field] and [reverse]
    fun sort(field: String): Boolean {
        reverse = if (this.field == field) !reverse else false
        this.field = field
        updateResult()

        return reverse
    }

}
package com.example.inventoryapp.viewmodel

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inventoryapp.database.STAFFS
import com.example.inventoryapp.database.Staff
import com.google.firebase.firestore.ktx.toObjects

class StaffViewModel : ViewModel() {
    // TODO: Initialization
    private var sta = listOf<Staff>()
    private val result = MutableLiveData<List<Staff>>()

    private var name = ""       // Search
    private var field = ""      // Sort
    private var reverse = false // Sort

    init {
            STAFFS.addSnapshotListener { snap, _ ->
                if (snap == null) return@addSnapshotListener

                sta = snap.toObjects<Staff>()
                updateResult()
            }
    }

    fun get(id: String): Staff? {
        return result.value?.find { s -> s.id == id }
    }

    fun getAll() = result

    fun delete(id: String) {
        // TODO
        STAFFS.document(id).delete()
    }

    fun deleteAll() {
        // TODO
        result.value?.forEach { s -> delete(s.id) }
    }

    fun set(s: Staff) {
        // TODO
        STAFFS.document(s.id).set(s)
    }

    private fun idExists(id: String): Boolean {
        return result.value?.any { s -> s.id == id } ?: false
    }

    private fun nameExists(name: String): Boolean {
        return result.value?.any { s -> s.name == name } ?: false
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validate(s: Staff, insert: Boolean = true): String {
        val regexId = Regex("""^[0-9A-Z]{5}$""")
        val regexName = Regex("""^[a-zA-Z]{0,6}(?: [a-zA-Z]+){0,2}$""")
        var e = ""

        if (insert) {
            e += if (s.id == "") "- Id is required.\n"
            else if (!s.id.matches(regexId)) "- Id format is invalid.\n"
            else if (idExists(s.id)) "- Id is duplicated.\n"
            else ""
        }

        if (insert) {
            e += if (s.name == "") "- Name is required.\n"
            else if (!s.name.matches(regexName)) "- Name format is invalid.\n"
            else if (s.name.length < 3) "- Name is too short.\n"
            else if (nameExists(s.name)) "- Name is duplicated.\n"
            else ""
        }

        if (insert) {
            e += if (s.email == "") "- Email is required.\n"
            else if (!isValidEmail(s.email)) "- Email format is invalid.\n"
            else ""
        }

        e += if (s.password == "") "- Password is required.\n"
        else ""

        if (insert) {
        e += if (s.phone == 0L) "- Phone is required.\n"
        else if (s.phone.toString().length < 10L) "- Invalid phone Number.\n"
        else ""
        }

        e += if (s.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }

    // TODO: Get search + filter + sort result
    private fun updateResult() {
        var list = sta

        // TODO: Search + filter
        list = list.filter { s ->
            s.name.contains(name, true)
        }

        // TODO: Sort
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
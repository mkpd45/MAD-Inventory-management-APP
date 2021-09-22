package com.example.inventoryapp.klModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.inventoryapp.database.LOCATIONS
import com.example.inventoryapp.database.Location
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await
import java.util.*

class LocationViewModel : ViewModel() {


    private val locations = MutableLiveData<List<Location>>()

    private var name = ""       // Search
    private var categoryId = "" // Filter
    private var field = ""      // Sort
    private var tabName = ""
    private var reverse = false // Sort

    init {

        LOCATIONS.addSnapshotListener { snap, _ ->
            if (snap == null) return@addSnapshotListener

            locations.value = snap.toObjects<Location>()

        }

    }

    //   suspend fun get(id: String): Friend? {
//        // TODO
//        return col.document(id).get().await().toObject<Friend>()
//    }
    fun get(lId: String): Location? {
        return locations.value?.find { l ->
            l.locationId == lId
        }
    }

    suspend fun getProducts(): List<Location> {
        return LOCATIONS.get().await().toObjects<Location>()
    }

    fun delete(id: String) {
        LOCATIONS.document(id).delete()
    }

    fun deleteAll() {
//        col.get().addOnSuccessListener { snap -> snap.documents.forEach{doc -> delete(doc.id) } }
        locations.value?.forEach { p -> delete(p.locationId) }
    }

    fun set(l: Location) {
        LOCATIONS.document(l.locationId).set(l)
    }

    fun addSlot(l: Location) {
        LOCATIONS.document(l.locationId).update("rackSlot", l.rackSlot)
    }

    fun getResult() = locations
}
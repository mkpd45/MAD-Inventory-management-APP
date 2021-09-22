package com.example.inventoryapp.klModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.inventoryapp.database.FLOWS
import com.example.inventoryapp.database.Flow
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class FlowViewModel : ViewModel() {


//    private var productsList = listOf<Product>()
      private val flows = MutableLiveData<List<Flow>>()
//
//    private var name = ""       // Search
//    private var categoryId = "" // Filter
//    private var field = ""      // Sort
//    private var tabName = ""
//    private var reverse = false // Sort
//
    init {

    FLOWS.addSnapshotListener { snap, _ ->
        if (snap == null) return@addSnapshotListener
        flows.value = snap.toObjects<Flow>()

    }
}
//        updateResult()
//    }

    //   suspend fun get(id: String): Friend? {
//        // TODO
//        return col.document(id).get().await().toObject<Friend>()
//    }

    fun get(flowId: String): Flow? {
        return flows.value?.find { f ->
            f.flowId == flowId
        }
    }

    fun getLastFlowId(): Int? {
        return flows.value?.last()?.flowId?.toInt()

    }

    suspend fun getFlows(): List<Flow> {
        return FLOWS.get().await().toObjects<Flow>()
    }

    fun delete(id: String) {
        FLOWS.document(id).delete()
    }

    fun deleteAll() {
//        col.get().addOnSuccessListener { snap -> snap.documents.forEach{doc -> delete(doc.id) } }
        flows.value?.forEach { f -> delete(f.flowId) }
    }

    fun set(f: Flow) {
        FLOWS.document(f.flowId).set(f)
    }
    fun getResult() = flows

    //----------------------------------------------------------------------------------------------

    // private suspend fun idExists(id: String): Boolean {
//        // TODO: Duplicated id?
//        col.document(id).get().await().exists()
//        return false
//    }
//    private fun idExists(id: Int): Boolean {
//        return flows.value?.any {p -> p.flowId ==id  }?:false
//    }
//
//    //   private suspend fun nameExists(name: String): Boolean {
////        // TODO: Duplicated name?
////        return col.whereEqualTo("name",name).get().await().size()>0
////    }
//    private fun nameExists(name: String): Boolean {
//        return flows.value?.any {p -> p.name == name  }?:false
//
//    }
//    fun validate(p: Product, add: Boolean = true): String {
//
//        var e = ""
//
//        if (add) {
//            e += if (idExists(p.barcode)) "- Id is duplicated.\n" else ""
//
//            e += if (nameExists(p.name)) "- Name is duplicated.\n" else ""
//        }
//
//        e += if (p.name == "") "- Name is required.\n"
//        else if (p.name.length < 3) "- Name is too short.\n"
//        else ""
//
//        e += if (p.price == 0.00) "- Price is required.\n"
//        else ""
//
//        e += if(p.category == "") "- The food category is required." else ""
//
//        e += if (p.photo.toBytes().isEmpty()) "- Photo is required.\n"
//        else ""
//
//
//        return e
//    }


    // TODO(15): Get search + filter + sort result
//    private fun updateResult() {

//        var list = productsList
//
//        // TODO(23): Search + filter (searching)
////        list = list.filter { p ->
////            p.name.contains(name, true) &&
////                    (categoryId == "" || categoryId == p.categoryId)
////        }
//
//        //Filter by tab item
//        list = if(tabName != "All") {
//            list.filter { p ->
//                p.category == tabName
//            }
//        } else
//            productsList

//        // TODO(24): Sort
//        list = when (field) {
//            "id" -> list.sortedBy { f -> f.barcode }
//            "name" -> list.sortedBy { f -> f.name }
//            "price" -> list.sortedBy { f -> f.price }
//            else -> list
//        }
//
//        if(reverse) list = list.reversed()

//        flows.value = list


    // ---------------------------------------------------------------------------------------------


//    // TODO(17): Return all categories
////    suspend fun getCategories(): List<Category> {
////        return CATEGORIES.get().await().toObjects<Category>()
////    }
//
//    // TODO(21): Set [name] -> update result
//    fun search(name: String) {
//        this.name = name
//        updateResult()
//    }
//
//    // TODO(22): Set [categoryId] -> update result
//    fun filter(categoryId: String) {
//        this.categoryId = categoryId
//        updateResult()
//    }
//
//    fun tabFilter(tabName: String){
//        this.tabName = tabName
//        updateResult()
//    }
//
//    // TODO(25): Set [field] and [reverse] -> update result (three - button filter work)
//    fun sort(field: String): Boolean {
//        reverse = if(this.field == field) !reverse else false
//        this.field = field
//
//        updateResult()
//        return reverse
//    }

    // ---------------------------------------------------------------------------------------------


}
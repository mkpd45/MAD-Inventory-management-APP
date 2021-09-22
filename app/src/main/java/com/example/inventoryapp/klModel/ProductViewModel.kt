package com.example.inventoryapp.klModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.database.PRODUCTS
import com.example.inventoryapp.database.Product
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class ProductViewModel : ViewModel() {


    private var productsList = listOf<Product>()
    private val products = MutableLiveData<List<Product>>()

    private var name = ""       // Search
    private var categoryId = "" // Filter
    private var field = ""      // Sort
    private var tabName = ""
    private var reverse = false // Sort

    init{

        PRODUCTS.addSnapshotListener{snap, _ ->
            if(snap == null) return@addSnapshotListener
            productsList = snap.toObjects<Product>()
            products.value = snap.toObjects<Product>()

        }
        updateResult()
    }

    //   suspend fun get(id: String): Friend? {
//        // TODO
//        return col.document(id).get().await().toObject<Friend>()
//    }
    fun get(barcode: String): Product? {
        return products.value?.find{
                p-> p.barcode == barcode
        }
    }

    suspend fun getProducts(): List<Product>{
        return PRODUCTS.get().await().toObjects<Product>()
    }

    fun delete(id: String) {
        PRODUCTS.document(id).delete()
    }

    fun deleteAll() {
//        col.get().addOnSuccessListener { snap -> snap.documents.forEach{doc -> delete(doc.id) } }
        products.value?.forEach { p-> delete(p.barcode) }
    }

    fun set(p: Product) {
        PRODUCTS.document(p.barcode).set(p)
    }

    fun moveStock(p:Product){
        PRODUCTS.document(p.barcode).update("stockQty",p.stockQty)
    }

    //----------------------------------------------------------------------------------------------

    // private suspend fun idExists(id: String): Boolean {
//        // TODO: Duplicated id?
//        col.document(id).get().await().exists()
//        return false
//    }
    private fun idExists(id: String): Boolean {
        return products.value?.any {p -> p.barcode ==id  }?:false
    }

    //   private suspend fun nameExists(name: String): Boolean {
//        // TODO: Duplicated name?
//        return col.whereEqualTo("name",name).get().await().size()>0
//    }
    private fun nameExists(name: String): Boolean {
        return products.value?.any {p -> p.name == name  }?:false

    }
    fun validate(p: Product, add: Boolean = true): String {

        var e = ""

        if (add) {
            e += if (idExists(p.barcode)) "- Id is duplicated.\n" else ""

            e += if (nameExists(p.name)) "- Name is duplicated.\n" else ""
        }

        e += if (p.name == "") "- Name is required.\n"
        else if (p.name.length < 3) "- Name is too short.\n"
        else ""

        e += if (p.price == 0.00) "- Price is required.\n"
        else ""

        e += if(p.category == "") "- The food category is required." else ""

        e += if (p.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""


        return e
    }


    // TODO(15): Get search + filter + sort result
    private fun updateResult() {

        var list = productsList

        // TODO(23): Search + filter (searching)
//        list = list.filter { p ->
//            p.name.contains(name, true) &&
//                    (categoryId == "" || categoryId == p.categoryId)
//        }

        //Filter by tab item
        list = if(tabName != "All") {
            list.filter { p ->
                p.category == tabName
            }
        } else
            productsList

//        // TODO(24): Sort
//        list = when (field) {
//            "id" -> list.sortedBy { f -> f.barcode }
//            "name" -> list.sortedBy { f -> f.name }
//            "price" -> list.sortedBy { f -> f.price }
//            else -> list
//        }
//
//        if(reverse) list = list.reversed()

        products.value = list
    }

    // ---------------------------------------------------------------------------------------------

    fun getResult() = products

    // TODO(17): Return all categories
//    suspend fun getCategories(): List<Category> {
//        return CATEGORIES.get().await().toObjects<Category>()
//    }

    // TODO(21): Set [name] -> update result
    fun search(name: String) {
        this.name = name
        updateResult()
    }

    // TODO(22): Set [categoryId] -> update result
    fun filter(categoryId: String) {
        this.categoryId = categoryId
        updateResult()
    }

    fun tabFilter(tabName: String){
        this.tabName = tabName
        updateResult()
    }

    // TODO(25): Set [field] and [reverse] -> update result (three - button filter work)
    fun sort(field: String): Boolean {
        reverse = if(this.field == field) !reverse else false
        this.field = field

        updateResult()
        return reverse
    }

    // ---------------------------------------------------------------------------------------------


}
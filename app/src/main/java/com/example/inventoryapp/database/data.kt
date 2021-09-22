package com.example.inventoryapp.database

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

data class Role(
    @DocumentId
    var id : String = "",
    var name: String ="",
){
    @get:Exclude
    var count: Int = 0

    override fun toString() = name
}

data class Staff(
    @DocumentId
    var id : String = "",
    var name : String = "",
    var email : String = "",
    var phone : Long = 0,
    var password : String = "",
    var role : String = "",
    var date : Date = Date(),
    var photo : Blob = Blob.fromBytes(ByteArray(0)),
    var roleId: String = "",
){
    @get:Exclude
    var category: Role = Role()
}

data class Supplier(
    @DocumentId
    var id : String = "",
    var name : String = "",
    var email : String = "",
    var phone : Long = 0,
    var lati: Double = 0.0,
    var longi : Double = 0.0,
    var location : GeoPoint = GeoPoint(lati,longi),
    var address : String = "",
    var date : Date = Date(),
    var photo : Blob = Blob.fromBytes(ByteArray(0)),
)
data class Product(
    @DocumentId
    var barcode: String = "",
    var name: String="",
    var price: Double = 0.00,
    var stockQty: Int = 0,
    var category: String = "",
    var supplier: String = "",
    var photo: Blob = Blob.fromBytes(ByteArray(0))
){
    @get:Exclude
    var count: Int = 0
}

data class Flow(
    @DocumentId
    var flowId: String = "",
    var pBarcode: String ="",
    var operation: String="",
    var qtyBefore: Int = 0,
    var qtyChange: Int = 0,
    var qtyAfter: Int = 0,
    var date: String = ""
){
    @get:Exclude
    var count: Int = 0
}

data class Location(
    @DocumentId
    var locationId: String = "",
    var placeTag: String ="",
    var rackSlot: Int = 0,
){
    @get:Exclude
    var count: Int = 0
}

val STAFFS = Firebase.firestore.collection("staffs")
val ROLES = Firebase.firestore.collection("roles")
val SUPPLIERS = Firebase.firestore.collection("suppliers")
val PRODUCTS = Firebase.firestore.collection("products")
val FLOWS = Firebase.firestore.collection("flows")
val LOCATIONS = Firebase.firestore.collection("locations")
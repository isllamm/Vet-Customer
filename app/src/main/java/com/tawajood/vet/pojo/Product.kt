package com.tawajood.vet.pojo

data class Product (
    val id: Int,
    val name: String,
    val desc:String,
    val subcat_id:Int,
    val price:Double ,
    val vendor_id:Int,
    val images:MutableList<ProductImages>,
        )
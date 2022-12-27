package com.tawajood.vet.pojo

data class VendorsData(
    val current_page: Int,
    val data: MutableList<Vendor>,
    val first_page_url:String,
    )
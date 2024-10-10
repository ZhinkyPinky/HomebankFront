package com.example.homebankfront.data.bodies

import java.time.LocalDateTime

data class Customer (
    val id : Long = -1L,
    val name : String = "",
    val description : String = "",
    val typeOfCustomerCode : String? = null,
    val rowCreatedBy : String? = null,
    val rowCreatedDate : LocalDateTime? = null,
    val rowLastEditBy : String? = null,
    val rowLastEditDate : LocalDateTime? = null,
    val rowVersion : LocalDateTime? = null
)
package com.example.homebankfront.data.bodies

import java.time.LocalDateTime

data class Customer(
    val id: Long = -1L,
    val name: String = "",
    val description: String = "",
    val typeOfCustomerCode: String? = null,
    val customerAmount: Int = 0,
    val rowVersion: LocalDateTime? = null,
)
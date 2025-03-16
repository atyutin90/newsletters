package com.example.newsletters.dto

import java.time.LocalDate

data class RequestDto(
    val id: Long? = null,
    val debtorId: Long,
    val destinationId: Long? = null,
    val date: LocalDate? = null,
    val dateFrom: LocalDate? = null,
    val dateTo: LocalDate? = null,
    val address: String? = null,
)

package com.example.newsletters.dto.report

import java.time.LocalDate

data class Request(
    val id: Long? = null,
    val destinationDetail: String? = null,
    val destination: String? = null,
    val date: LocalDate? = null,
    val dateFrom: LocalDate? = null,
    val dateTo: LocalDate? = null,
    val address: String? = null,
    val accounts: Set<String> = setOf(),
)

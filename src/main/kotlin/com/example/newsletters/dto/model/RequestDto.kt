package com.example.newsletters.dto.model

import java.time.LocalDate

data class RequestDto(
    val id: Long? = null,
    val debtorId: Long,
    val destinationId: Long? = null,
    val destinationDetail: String? = null,
    val date: LocalDate? = null,
    val dateFrom: LocalDate? = null,
    val dateTo: LocalDate? = null,
    val address: String? = null,
    //Банковские счета перечисленные через запятную или точку с запятой
    val accounts: String? = null,
)

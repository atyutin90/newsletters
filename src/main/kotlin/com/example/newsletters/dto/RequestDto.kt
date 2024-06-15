package com.example.newsletters.dto

import java.time.ZonedDateTime

data class RequestDto(
    val id: Long? = null,
    val companyAddress: String? = null,
    val companyName: String? = null,
    val dateFrom: ZonedDateTime? = null,
    val dateTo: ZonedDateTime? = null,
    val attachment: String? = null
)

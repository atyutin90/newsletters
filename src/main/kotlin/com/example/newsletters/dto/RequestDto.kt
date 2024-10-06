package com.example.newsletters.dto

import java.time.ZonedDateTime

data class RequestDto(
    val id: Long? = null,
    val requestDestinationId: Long? = null,
    val date: ZonedDateTime? = null,
    val dateFrom: ZonedDateTime? = null,
    val dateTo: ZonedDateTime? = null,
)

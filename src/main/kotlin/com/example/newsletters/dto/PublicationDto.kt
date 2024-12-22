package com.example.newsletters.dto

import java.time.LocalDate
import java.time.LocalTime

data class PublicationDto(
    val id: Long? = null,
    val publicationDate: LocalDate? = null,
    val appointmentDate: LocalDate? = null,
    val appointmentTime: LocalTime? = null,
    val hallNumber: String? = null,
    val kommersantMessageNumber: String? = null,
    val efrsbMessageNumber: String? = null,
    val kommersantIssueNumber: String? = null,
    val debtorId: Long,
)

package com.example.newsletters.dto

import java.time.LocalDate
import java.time.LocalTime

data class WorkerMeetingDto(
    val id: Long? = null,
    val debtorId: Long,
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val registrationTimeFrom: LocalTime? = null,
    val registrationTimeTo: LocalTime? = null,
    val address: String? = null,
    val topic: String? = null,
)

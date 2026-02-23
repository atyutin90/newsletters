package com.example.newsletters.dto.report

import java.time.LocalDate
import java.time.LocalTime

data class DebtorMeeting(
    val id: Long? = null,
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val registrationTimeFrom: LocalTime? = null,
    val registrationTimeTo: LocalTime? = null,
    val address: String? = null,
    val familiarizationDateFrom: LocalDate? = null,
    val familiarizationDateTo: LocalDate? = null,
)

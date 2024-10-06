package com.example.newsletters.dto

import java.time.ZonedDateTime

data class DebtorDto(
    val id: Long? = null,
    val fullName: String? = null,
    val name: String? = null,
    val caseNumber: String? = null,
    val address: String? = null,
    val courtAct: String? = null,
    val actDate: ZonedDateTime? = null,
    val resolutionDate: ZonedDateTime? = null,
    val taxRegistrationReasonCode: String? = null,
    val taxpayerIdentificationNumber: String? = null,
    var primaryStateRegistrationNumber: String? = null,
    val registryDate: ZonedDateTime? = null,
    val registryClosingDate: ZonedDateTime? = null,
    val debtorMeeting: DebtorMeetingDto? = null,
    val requests: List<RequestDto> = listOf()
)

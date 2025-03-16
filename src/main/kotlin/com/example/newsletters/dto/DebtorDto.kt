package com.example.newsletters.dto

import java.time.LocalDate

data class DebtorDto(
    val id: Long? = null,
    val arbitrationManagerId: Long? = null,
    val fullName: String? = null,
    val name: String? = null,
    val caseNumber: String? = null,
    val address: String? = null,
    val courtAct: String? = null,
    val actDate: LocalDate? = null,
    val resolutionDate: LocalDate? = null,
    val taxRegistrationReasonCode: String? = null,
    val taxpayerIdentificationNumber: String? = null,
    var primaryStateRegistrationNumber: String? = null,
    val registryDate: LocalDate? = null,
    val registryClosingDate: LocalDate? = null,
    val debtorMeeting: DebtorMeetingDto? = null,
    val publications: List<PublicationDto> = mutableListOf(),
    var arbitrationCase: String? = null
)

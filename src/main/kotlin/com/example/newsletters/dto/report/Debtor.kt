package com.example.newsletters.dto.report

import java.time.LocalDate

data class Debtor(
    val id: Long? = null,
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
    val arbitrationCase: String? = null,
    val documentNumber: String? = null
)

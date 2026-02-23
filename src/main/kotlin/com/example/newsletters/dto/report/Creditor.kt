package com.example.newsletters.dto.report

import java.time.LocalDate

data class Creditor(
    val id: Long? = null,
    val clientType: String? = null,
    val name: String? = null,
    val taxpayerIdentificationNumber: String? = null,
    val primaryStateRegistrationNumber: String? = null,
    val passportSerial: Int? = null,
    val passportNumber: Int? = null,
    val address: String? = null,
    val executionWrit : String? = null,
    val executionDate : LocalDate? = null
)

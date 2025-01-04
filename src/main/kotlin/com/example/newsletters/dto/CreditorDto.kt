package com.example.newsletters.dto
import java.math.BigDecimal
import java.time.LocalDate

data class CreditorDto(
    val id: Long? = null,
    val debtorId: Long? = null,
    val clientType: String? = null,
    val name: String? = null,
    val taxpayerIdentificationNumber: String? = null,
    val primaryStateRegistrationNumber: String? = null,
    val passportSerial: String? = null,
    val passportNumber: String? = null,
    val address: String? = null,
    val principalAmount: BigDecimal? = null,
    val stateDutyAmount : BigDecimal? = null,
    val executionWrit : String? = null,
    val executionDate : LocalDate? = null
)

package com.example.newsletters.dto

import jakarta.persistence.Column
import java.math.BigDecimal
import java.time.LocalDate

data class QueueDto(
    val id : Long? = null,
    val creditorId: Long? = null,
    val type: String? = null,
    val entryDate: LocalDate? = null,
    val obligationType: String? = null,
    val documentNumberOfReasonClaim: String? = null,
    val claimDate: LocalDate? = null,
    val claimAmount: BigDecimal? = null,
    val determination: String? = null,
    val repaymentDate: LocalDate? = null,
    val repaymentDocumentDetails: String? = null,
    val outstandingAmount: BigDecimal? = null,
    val exclusionFromRegisterDate: LocalDate? = null,
    val exclusionDocument: String? = null,
    val depositLocation: String? = null,
    val depositDocumentDetails: String? = null,
    val depositAmount: BigDecimal? = null,
    val fineType: String? = null,
    val fineAmount: BigDecimal? = null,
    val principalAmount: BigDecimal? = null,
    val stateDutyAmount: BigDecimal? = null,
    val percentAmount: BigDecimal? = null,
    val penaltyAmount : BigDecimal? = null,
    val fine: BigDecimal? = null,
    val percentOnPercentAmount: BigDecimal? = null,
)

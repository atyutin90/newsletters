package com.example.newsletters.dto

import java.math.BigDecimal
import java.time.LocalDate

data class QueueDto(
    val id : Long? = null,
    val creditorId: Long? = null,
    val type: String? = null,
    val amount: BigDecimal? = null,
    val entryDate: LocalDate? = null,
    val obligationType: String? = null,
    val documentNumberOfReasonClaim: String? = null,
    val claimDate: LocalDate? = null,
    val claimAmount: BigDecimal? = null,
    val determination: String? = null,
    val repaymentDate: LocalDate? = null,
    val repaymentDocumentDetails: String? = null,
    val repaymentAmount: BigDecimal? = null,
    val outstandingAmount: BigDecimal? = null,
    val exclusionFromRegisterDate: LocalDate? = null,
    val exclusionDocument: String? = null,
    val depositLocation: String? = null,
    val depositDocumentDetails: String? = null,
    val depositAmount: BigDecimal? = null,
    val fineType: String? = null,
    val fineAmount: BigDecimal? = null
)

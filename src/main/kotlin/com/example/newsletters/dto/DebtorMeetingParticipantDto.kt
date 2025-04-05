package com.example.newsletters.dto

data class DebtorMeetingParticipantDto(
    val id: Long? = null,
    val debtorMeetingId: Long,
    var fullName: String? = null,
    val address: String? = null,
)

package com.example.newsletters.dto

data class DebtorMeetingQuestionDto(
    val id: Long? = null,
    val debtorMeetingId: Long,
    val question: String? = null,
    val answer: String? = null,
    val position: Int? = null,
)

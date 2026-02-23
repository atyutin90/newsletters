package com.example.newsletters.dto.model

data class DebtorMeetingQuestionDto(
    val id: Long? = null,
    val debtorMeetingId: Long,
    val question: String? = null,
    val proposedSolution: String? = null,
    val acceptedSolution: String? = null,
    val answer: String? = null,
    val position: Int? = null,
)

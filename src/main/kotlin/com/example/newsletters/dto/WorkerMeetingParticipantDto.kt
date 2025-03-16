package com.example.newsletters.dto

data class WorkerMeetingParticipantDto(
    val id: Long? = null,
    val workerMeetingId: Long,
    var fullName: String? = null,
    val address: String? = null,
)

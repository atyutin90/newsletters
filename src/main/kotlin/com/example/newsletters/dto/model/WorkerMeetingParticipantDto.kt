package com.example.newsletters.dto.model

data class WorkerMeetingParticipantDto(
    val id: Long? = null,
    val workerMeetingId: Long,
    var fullName: String? = null,
    val address: String? = null,
)

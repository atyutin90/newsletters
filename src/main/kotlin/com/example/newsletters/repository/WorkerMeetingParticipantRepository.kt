package com.example.newsletters.repository

import com.example.newsletters.entity.WorkerMeetingParticipant
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface WorkerMeetingParticipantRepository : JpaRepository<WorkerMeetingParticipant, Long> {
    fun findByWorkerMeetingId(workerMeetingId: Long): List<WorkerMeetingParticipant>
}

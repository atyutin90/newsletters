package com.example.newsletters.repository

import com.example.newsletters.entity.WorkerMeeting
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface WorkerMeetingRepository : JpaRepository<WorkerMeeting, Long> {
    fun findByDebtorId(debtorId: Long): List<WorkerMeeting>
}
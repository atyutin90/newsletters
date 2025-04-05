package com.example.newsletters.repository

import com.example.newsletters.entity.DebtorMeetingParticipant
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface DebtorMeetingParticipantRepository : JpaRepository<DebtorMeetingParticipant, Long> {
    fun findByDebtorMeetingId(debtorMeetingId: Long): List<DebtorMeetingParticipant>
}

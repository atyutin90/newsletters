package com.example.newsletters.repository

import com.example.newsletters.entity.DebtorMeetingQuestion
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface DebtorMeetingQuestionRepository : JpaRepository<DebtorMeetingQuestion, Long> {
    fun findByDebtorMeetingId(debtorMeetingId: Long): List<DebtorMeetingQuestion>
}

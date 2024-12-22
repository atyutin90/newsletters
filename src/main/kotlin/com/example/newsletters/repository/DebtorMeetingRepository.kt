package com.example.newsletters.repository

import com.example.newsletters.entity.DebtorMeeting
import com.example.newsletters.entity.Request
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface DebtorMeetingRepository : JpaRepository<DebtorMeeting, Long> {
    fun findByDebtorId(debtorId: Long): List<DebtorMeeting>
}
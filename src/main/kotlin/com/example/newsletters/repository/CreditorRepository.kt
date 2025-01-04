package com.example.newsletters.repository

import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.Publication
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface CreditorRepository : JpaRepository<Creditor, Long> {

    fun findByDebtorId(debtorId: Long): List<Creditor>

}
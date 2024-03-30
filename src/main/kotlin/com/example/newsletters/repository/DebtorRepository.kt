package com.example.newsletters.repository

import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.Debtor
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface DebtorRepository : JpaRepository<Debtor, Int> {
    fun findByNameContainingIgnoreCase(keyword: String): List<Debtor>
}

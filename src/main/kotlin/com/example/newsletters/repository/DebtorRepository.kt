package com.example.newsletters.repository

import com.example.newsletters.entity.Debtor
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface DebtorRepository : JpaRepository<Debtor, Int> {
    fun findByNameContainingIgnoreCase(keyword: String): List<Debtor>
    fun findByNameContainingIgnoreCase(name: String, paging: Pageable): Page<Debtor>
}

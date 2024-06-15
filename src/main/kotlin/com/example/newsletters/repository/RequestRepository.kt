package com.example.newsletters.repository

import com.example.newsletters.entity.Request
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface RequestRepository : JpaRepository<Request, Long> {
    fun findByCompanyNameContainingIgnoreCase(name: String, paging: Pageable): Page<Request>
}
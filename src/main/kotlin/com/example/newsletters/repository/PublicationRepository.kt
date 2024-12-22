package com.example.newsletters.repository

import com.example.newsletters.entity.Publication
import com.example.newsletters.entity.Request
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface PublicationRepository : JpaRepository<Publication, Long> {
    fun findByDebtorId(debtorId: Long): List<Publication>
}

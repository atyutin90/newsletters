package com.example.newsletters.repository

import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.Debtor
import com.example.newsletters.entity.Location
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface LocationRepository : JpaRepository<Location, Int> {
    fun findByNameContainingIgnoreCase(keyword: String): List<Location>
    fun findByNameContainingIgnoreCase(name: String, paging: Pageable): Page<Location>

}

package com.example.newsletters.repository

import com.example.newsletters.entity.RequestDestination
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Transactional
@Repository
interface RequestDestinationRepository : JpaRepository<RequestDestination, Long> {

    fun findByNameContainingIgnoreCase(keyword: String): List<RequestDestination>

    fun findByNameContainingIgnoreCase(name: String, paging: Pageable): Page<RequestDestination>
}
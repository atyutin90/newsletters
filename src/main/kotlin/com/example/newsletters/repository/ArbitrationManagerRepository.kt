package com.example.newsletters.repository

import com.example.newsletters.entity.ArbitrationManager
import com.example.newsletters.entity.Court
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Transactional
@Repository
interface ArbitrationManagerRepository : JpaRepository<ArbitrationManager, Long> {

    fun findByFullNameContainingIgnoreCase(keyword: String): List<ArbitrationManager >

    fun findByFullNameContainingIgnoreCase(keyword: String, paging: Pageable): Page<ArbitrationManager>
}

package com.example.newsletters.repository

import com.example.newsletters.entity.FileDB
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Transactional
@Repository
interface FileDBRepository : JpaRepository<FileDB, Int> {

    fun findByNameContainingIgnoreCase(keyword: String): List<FileDB>
    fun findByNameContainingIgnoreCaseOrderByCreatedAtDesc(name: String, paging: Pageable): Page<FileDB>
    fun findByOrderByCreatedAtDesc(paging: Pageable): Page<FileDB>
}

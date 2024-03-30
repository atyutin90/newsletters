package com.example.newsletters.repository

import com.example.newsletters.entity.FileDB
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Transactional
@Repository
interface FileDBRepository : JpaRepository<FileDB, Int> {
    fun findByNameContainingIgnoreCase(keyword: String): List<FileDB>
}

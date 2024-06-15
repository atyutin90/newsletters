package com.example.newsletters.repository

import com.example.newsletters.entity.Queue
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface QueueRepository : JpaRepository<Queue, Long> {
    fun findByCreditorId(creditorId: Long): List<Queue>
    @Query("SELECT p FROM Queue p WHERE p.creditorId in (:creditorIds)")
    fun findByCreditorIds(creditorIds: List<Long>): List<Queue>
}

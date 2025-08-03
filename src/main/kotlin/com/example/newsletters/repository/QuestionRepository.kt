package com.example.newsletters.repository

import com.example.newsletters.entity.Question
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : JpaRepository<Question, Long> {
    fun findAllByOrderByPositionAsc(pageable: Pageable): Page<Question>
    fun findAllByOrderByPositionAsc(): List<Question>
    fun findByValueContainingIgnoreCase(keyword: String): List<Question>
    fun findByValueContainingIgnoreCase(name: String, paging: Pageable): Page<Question>
}

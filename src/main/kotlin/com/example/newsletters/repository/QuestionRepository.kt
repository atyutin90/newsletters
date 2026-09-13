package com.example.newsletters.repository

import com.example.newsletters.entity.Question
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
    override fun findAll(spec: Specification<Question>, pageable: Pageable): Page<Question>

    fun findAllByOrderByPositionAsc(pageable: Pageable): Page<Question>

    fun findAllByOrderByPositionAsc(): List<Question>
}

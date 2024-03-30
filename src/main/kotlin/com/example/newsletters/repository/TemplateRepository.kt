package com.example.newsletters.repository

import com.example.newsletters.dto.TemplateType

import com.example.newsletters.entity.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface TemplateRepository : JpaRepository<Template, Int> {

    fun findByTypeAndNameContainingIgnoreCase(type: TemplateType, keyword: String): List<Template>

    fun findByType(type: TemplateType): List<Template>
}

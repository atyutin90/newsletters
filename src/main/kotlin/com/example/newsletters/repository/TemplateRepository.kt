package com.example.newsletters.repository

import com.example.newsletters.dto.model.TemplateType
import com.example.newsletters.entity.Template
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface TemplateRepository : JpaRepository<Template, Long> {

    fun findByType(type: TemplateType): List<Template>

    fun findByType(type: TemplateType, paging: Pageable): Page<Template>
}

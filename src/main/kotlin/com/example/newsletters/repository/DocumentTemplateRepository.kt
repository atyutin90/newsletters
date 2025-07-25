package com.example.newsletters.repository

import com.example.newsletters.entity.DocumentTemplate

import com.example.newsletters.entity.enum.DocumentTemplateType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface DocumentTemplateRepository : JpaRepository<DocumentTemplate, Long> {

    fun findByNameContainingIgnoreCase(type: String, pageable: Pageable): Page<DocumentTemplate>

    fun findByType(type: DocumentTemplateType): List<DocumentTemplate>

    fun findByType(type: DocumentTemplateType, paging: Pageable): Page<DocumentTemplate>
}

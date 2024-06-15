package com.example.newsletters.repository

import com.example.newsletters.entity.Document
import com.example.newsletters.entity.DocumentTemplate
import com.example.newsletters.entity.WorkerMeeting

import com.example.newsletters.entity.enum.DocumentTemplateType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface DocumentRepository : JpaRepository<Document, Long> {

    fun findByDebtorIdOrderByName(debtorId: Long): List<Document>

    fun findByDebtorIdAndType(debtorId: Long, type: DocumentTemplateType): List<Document>

    fun findByType(type: DocumentTemplateType, paging: Pageable): Page<Document>

    fun deleteByDebtorId(debtorId: Long)
}

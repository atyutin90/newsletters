package com.example.newsletters.service

import com.example.newsletters.dto.model.DocumentTemplateDto
import com.example.newsletters.entity.DocumentTemplate
import com.example.newsletters.entity.enum.DocumentTemplateType
import com.example.newsletters.entity.enum.DocumentTemplateType.Companion.documentTemplateTypeOf
import com.example.newsletters.repository.DocumentTemplateRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DocumentTemplateStorageService(
    val repository: DocumentTemplateRepository,
) {
    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.documentTemplateDto }

    fun getByName(name: String, paging: Pageable) = repository.findByNameContainingIgnoreCase(name, paging)
        .map { it.documentTemplateDto }

    fun getByType(type: DocumentTemplateType) = repository.findByType(type).map { it.documentTemplateDto }

    fun getById(id: Long) = repository.findById(id).map { it.documentTemplateDto }.orElse(null)

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).mapNotNull { it.documentTemplateDto }

    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun save(data: DocumentTemplateDto) {
        val documentTemplateType = documentTemplateTypeOf(data.type)
        val existDocumentTemplate = data.id?.let { repository.findById(it).orElse(null) }
        val existUniqueDocumentTemplate =
            if (documentTemplateType?.multiple == false) repository.findByType(documentTemplateType)
                .firstOrNull() else null
        if (existUniqueDocumentTemplate != null) {
            data.copy(id = existUniqueDocumentTemplate.id).let { it -> repository.save(it.documentTemplate) }
        } else if (existDocumentTemplate != null) {
            data.copy(id = existDocumentTemplate.id).let { it -> repository.save(it.documentTemplate) }
        } else {
            repository.save(data.documentTemplate)
        }
    }

    val DocumentTemplateDto.documentTemplate
        get() = DocumentTemplate(
            id = id,
            type = documentTemplateTypeOf(type),
            name = name,
            resourcePath = resourcePath
        )

    val DocumentTemplate.documentTemplateDto
        get() = DocumentTemplateDto(
            id = id,
            type = type?.name,
            name = name,
            resourcePath = resourcePath
        )
}

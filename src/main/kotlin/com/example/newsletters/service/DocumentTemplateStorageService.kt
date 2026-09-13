package com.example.newsletters.service

import com.example.newsletters.dto.filter.specification.DocumentTemplateSpecification.Companion.documentTemplateFilterSpecification
import com.example.newsletters.dto.model.DocumentTemplateDto
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.entity.DocumentTemplate
import com.example.newsletters.entity.enum.DocumentTemplateType
import com.example.newsletters.entity.enum.DocumentTemplateType.Companion.documentTemplateTypeOf
import com.example.newsletters.repository.DocumentTemplateRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DocumentTemplateStorageService(
    val repository: DocumentTemplateRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(paging: Pageable) =
        repository.findAll(paging)
            .map { it.documentTemplateDto }

    fun getAll(filter: PageFilter, paging: Pageable) =
        repository.findAll(documentTemplateFilterSpecification(filter), paging)
            .map { it.documentTemplateDto }

    fun getByType(type: DocumentTemplateType) =
        repository.findByType(type)
            .map { it.documentTemplateDto }

    fun getById(id: Long): DocumentTemplateDto =
        repository.findById(id)
            .map { it.documentTemplateDto }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) =
        repository.findAllById(ids)
            .mapNotNull { it.documentTemplateDto }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun save(data: DocumentTemplateDto): DocumentTemplateDto {
        val documentTemplateType = documentTemplateTypeOf(data.type)
        val existDocumentTemplate = data.id?.let { repository.findById(it).orElse(null) }
        val existUniqueDocumentTemplate =
            if (documentTemplateType?.multiple == false) repository.findByType(documentTemplateType)
                .firstOrNull() else null
        val saved = if (existUniqueDocumentTemplate != null) {
            repository.save(data.copy(id = existUniqueDocumentTemplate.id).documentTemplate)
        } else if (existDocumentTemplate != null) {
            repository.save(data.copy(id = existDocumentTemplate.id).documentTemplate)
        } else {
            repository.save(data.documentTemplate)
        }
        return saved.documentTemplateDto
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

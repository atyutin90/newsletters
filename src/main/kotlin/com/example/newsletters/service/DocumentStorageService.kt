package com.example.newsletters.service

import com.example.newsletters.dto.model.DocumentDto
import com.example.newsletters.entity.Document
import com.example.newsletters.entity.enum.DocumentTemplateType.Companion.documentTemplateTypeOf
import com.example.newsletters.repository.DocumentRepository
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DocumentStorageService(
    val repository: DocumentRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getById(id: Long): DocumentDto =
        repository.findById(id)
            .map { it.documentDto }
            .orElseThrow { notExist(id) }

    fun getByDebtorId(debtorId: Long): List<DocumentDto> =
        repository.findByDebtorIdOrderByName(debtorId)
            .map { it.documentDto }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun save(dto: DocumentDto) {
        documentTemplateTypeOf(dto.type)?.let { type ->
            repository.findByDebtorIdAndType(dto.debtorId, type).firstOrNull()
                ?.let { dto.copy(id = it.id) }
        }?.document?.let { repository.save(it) }
    }

    val DocumentDto.document
        get() = Document(
            id = id,
            type = documentTemplateTypeOf(type),
            name = name,
            debtorId = debtorId,
            templateResourcePath = templateResourcePath,

            )

    val Document.documentDto
        get() = DocumentDto(
            id = id,
            type = type?.name,
            debtorId = debtorId,
            name = name,
            templateResourcePath = templateResourcePath,
        )
}

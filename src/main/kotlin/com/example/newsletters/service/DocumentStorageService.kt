package com.example.newsletters.service

import com.example.newsletters.dto.DocumentDto
import com.example.newsletters.entity.Document
import com.example.newsletters.entity.enum.DocumentTemplateType.Companion.documentTemplateTypeOf
import com.example.newsletters.repository.DocumentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class DocumentStorageService(
    val repository: DocumentRepository,
    val valueListService: ValueListService,
) {

    fun getById(id: Long): DocumentDto? = repository.findById(id).map { it.documentDto }.orElse(null)

    fun getByDebtorId(debtorId: Long): List<DocumentDto> = repository.findByDebtorId(debtorId).map { it.documentDto }

    fun delete(id: Long) = repository.deleteById(id)

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
            fileName = fileName,
            contentType = contentType,
            data = data,
            debtorId = debtorId
        )

    val Document.documentDto
        get() = DocumentDto(
            id = id,
            type = type?.name,
            name = name,
            fileName = fileName,
            contentType = contentType,
            data = data,
            debtorId = debtorId
        )
}

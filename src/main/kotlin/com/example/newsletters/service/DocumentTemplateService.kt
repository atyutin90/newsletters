package com.example.newsletters.service

import com.example.newsletters.dto.DocumentTemplateDto
import com.example.newsletters.entity.DocumentTemplate
import com.example.newsletters.entity.enum.DocumentTemplateType
import com.example.newsletters.entity.enum.DocumentTemplateType.Companion.documentTemplateTypeOf
import com.example.newsletters.repository.DocumentTemplateRepository
import com.example.newsletters.utils.FileUtils.extensionFile
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils.cleanPath
import org.springframework.web.multipart.MultipartFile

@Service
class DocumentTemplateService(
    val repository: DocumentTemplateRepository,
) {
    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.documentTemplateDto }

    fun getByName(name: String, paging: Pageable) = run {
        repository.findByNameContainingIgnoreCase(name, paging)
            .map { it.documentTemplateDto }
    }

    fun getByType(type: DocumentTemplateType) = repository.findByType(type).map { it.documentTemplateDto }

    fun getById(id: Long) = repository.findById(id).map { it.documentTemplateDto }.orElse(null)

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).mapNotNull { it.documentTemplateDto }

    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun save(data: DocumentTemplateDto, file: MultipartFile) = saveProcess(data, file)

    private fun saveProcess(data: DocumentTemplateDto, file: MultipartFile) {
        val type = documentTemplateTypeOf(data.type)
        val existDocumentTemplate = if (type?.multiple == false) repository.findByType(type).firstOrNull() else null
        val extensionFile = (file.originalFilename?.let { cleanPath(it) }?.takeIf { it.isNotEmpty() }
            ?: existDocumentTemplate?.fileName)?.extensionFile()

        fun modifyExistNoMultipleDocumentTypeDto() = data.let { dtd ->
            if (!file.isEmpty) {
                dtd.copy(
                    id = existDocumentTemplate?.id,
                    contentType = file.contentType,
                    data = file.bytes,
                    fileName = "${dtd.name}.$extensionFile",
                )
            } else {
                dtd.copy(
                    id = existDocumentTemplate?.id,
                    contentType = existDocumentTemplate?.contentType,
                    data = existDocumentTemplate?.data,
                    fileName = "${dtd.name}.$extensionFile",
                )
            }
        }

        fun modifyDocumentTypeDto() = data.let { dtd ->
            if (!file.isEmpty) {
                dtd.copy(
                    contentType = file.contentType,
                    data = file.bytes,
                    fileName = "${dtd.name}.$extensionFile",
                )
            } else {
                dtd.id?.let {
                    repository.findById(dtd.id).map {
                        dtd.copy(
                            contentType = it.contentType,
                            data = it.data,
                            fileName = "${dtd.name}.${it.fileName?.extensionFile()}",
                        )
                    }.orElse(dtd)
                } ?: dtd
            }
        }

        if (type?.multiple == false && existDocumentTemplate != null) {
            if (data.id == null || data.id == existDocumentTemplate.id) {
                modifyExistNoMultipleDocumentTypeDto().let { repository.save(it.documentTemplate) }
            }
        } else {
            modifyDocumentTypeDto().let { repository.save(it.documentTemplate) }
        }
    }

    val DocumentTemplateDto.documentTemplate
        get() = DocumentTemplate(
            id = id,
            type = documentTemplateTypeOf(type),
            name = name,
            fileName = fileName,
            contentType = contentType,
            data = data
        )

    val DocumentTemplate.documentTemplateDto
        get() = DocumentTemplateDto(
            id = id,
            type = type?.name,
            name = name,
            fileName = fileName,
            contentType = contentType,
            data = data
        )
}

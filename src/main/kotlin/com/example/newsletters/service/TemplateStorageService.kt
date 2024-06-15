package com.example.newsletters.service

import com.example.newsletters.dto.TemplateDto
import com.example.newsletters.dto.TemplateType
import com.example.newsletters.entity.Template
import com.example.newsletters.repository.TemplateRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class TemplateStorageService(val templateRepository: TemplateRepository) {

    fun store(name: String, type: TemplateType, file: MultipartFile): Template {
        val fileName: String = StringUtils.cleanPath(file.originalFilename!!)
        val template = Template(
            type = null,
            contentType = file.contentType,
            data = file.bytes
        )
        return templateRepository.save(template)
    }

    fun get(id: Long): TemplateDto? = templateRepository.findById(id).map { it.templateDto }.orElse(null)

    fun getByIds(ids: List<Long>) = templateRepository.findAllById(ids).map { it.templateDto }

    fun getAll() = templateRepository.findAll().map { it.templateDto }

    fun getByType(type: TemplateType) = templateRepository.findByType(type).map { it.templateDto }

    fun getByType(type: TemplateType, paging: Pageable) = templateRepository.findByType(type, paging).map { it.templateDto }

    fun delete(id: Long) = templateRepository.deleteById(id)
}

val Template.templateDto: TemplateDto
    get() = TemplateDto(
        id = this.id,
        type = null,
        contentType = this.contentType,
        data = this.data
    )

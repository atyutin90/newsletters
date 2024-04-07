package com.example.newsletters.service

import com.example.newsletters.dto.TemplateDto
import com.example.newsletters.dto.TemplateType
import com.example.newsletters.entity.Template
import com.example.newsletters.repository.TemplateRepository
import com.example.newsletters.utils.FileUtils.getExtensionFileByName
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class TemplateStorageService(val templateRepository: TemplateRepository) {

    fun store(name: String, type: TemplateType, file: MultipartFile): Template {
        val fileName: String = StringUtils.cleanPath(file.originalFilename!!)
        val template = Template("$name.${getExtensionFileByName(fileName)}", type, file.contentType, file.bytes)
        return templateRepository.save(template)
    }

    fun get(id: Int): TemplateDto? = templateRepository.findById(id).map { it.templateDto }.orElse(null)

    fun getByNameAndType(type: TemplateType, name: String) = templateRepository.findByTypeAndNameContainingIgnoreCase(type, name).map { it.templateDto }

    fun getByNameAndType(type: TemplateType, name: String, paging: Pageable) = templateRepository.findByTypeAndNameContainingIgnoreCase(type, name, paging).map { it.templateDto }

    fun getByIds(ids: List<Int>) = templateRepository.findAllById(ids).map { it.templateDto }

    fun getAll() = templateRepository.findAll().map { it.templateDto }

    fun getByType(type: TemplateType) = templateRepository.findByType(type).map { it.templateDto }

    fun getByType(type: TemplateType, paging: Pageable) = templateRepository.findByType(type, paging).map { it.templateDto }

    fun delete(id: Int) = templateRepository.deleteById(id)
}

val Template.templateDto: TemplateDto
    get() = TemplateDto(
        id = this.id,
        name = this.name,
        type = this.type,
        contentType = this.contentType,
        data = this.data
    )

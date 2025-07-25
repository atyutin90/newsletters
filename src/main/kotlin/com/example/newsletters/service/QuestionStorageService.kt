package com.example.newsletters.service

import com.example.newsletters.dto.DocumentTemplateDto
import com.example.newsletters.dto.QuestionDto
import com.example.newsletters.entity.DocumentTemplate
import com.example.newsletters.entity.Question
import com.example.newsletters.repository.QuestionRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class QuestionStorageService(val repository: QuestionRepository) {
    fun getAll(): List<QuestionDto> = repository.findAllByOrderByValueAsc().map { it.questionDto }
    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.questionDto }
    fun getByName(name: String) = repository.findByValueContainingIgnoreCase(name).map { it.questionDto }
    fun getByName(name: String, paging: Pageable) =
        repository.findByValueContainingIgnoreCase(name, paging).map { it.questionDto }

    fun getById(id: Long) = repository.findById(id).map { it.questionDto }.orElse(null)

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.questionDto }

    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun save(data: QuestionDto) = repository.save(data.question)
}

val Question.questionDto
    get() = QuestionDto(
        id = id,
        value = value,
        position = position,
        documentTemplateIds = documentTemplates.mapNotNull { it.id }.toSet()
    )

val QuestionDto.question
    get() = Question(
        id = id,
        value = value,
        position = position,
        documentTemplates = documentTemplateIds.map { DocumentTemplate(id = it) }.toSet()
    )

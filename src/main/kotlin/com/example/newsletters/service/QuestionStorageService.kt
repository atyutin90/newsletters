package com.example.newsletters.service

import com.example.newsletters.dto.filter.specification.QuestionSpecification.Companion.questionFilterSpecification
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.dto.model.QuestionDto
import com.example.newsletters.entity.Question
import com.example.newsletters.repository.QuestionRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QuestionStorageService(
    val repository: QuestionRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<QuestionDto> =
        repository.findAllByOrderByPositionAsc()
            .map { it.questionDto }

    fun getAll(paging: Pageable) =
        repository.findAllByOrderByPositionAsc(paging)
            .map { it.questionDto }

    fun getAll(filter: PageFilter, paging: Pageable) =
        repository.findAll(questionFilterSpecification(filter), paging)
            .map { it.questionDto }

    fun getById(id: Long): QuestionDto = repository.findById(id)
        .map { it.questionDto }
        .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) =
        repository.findAllById(ids)
            .map { it.questionDto }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun save(data: QuestionDto) = repository.save(data.question).questionDto
}

val Question.questionDto
    get() = QuestionDto(
        id = id,
        value = value,
        position = position,
        documentTemplateIds = documentTemplateIds
    )

val QuestionDto.question
    get() = Question(
        id = id,
        value = value,
        position = position,
        documentTemplateIds = documentTemplateIds
    )

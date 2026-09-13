package com.example.newsletters.service

import com.example.newsletters.dto.filter.specification.CourtSpecification.Companion.courtFilterSpecification
import com.example.newsletters.dto.model.CourtDto
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.entity.Court
import com.example.newsletters.repository.CourtRepository
import jakarta.transaction.Transactional
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CourtService(
    val repository: CourtRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<CourtDto> = repository.findAll().map { it.courtDto }

    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.courtDto }

    fun getAll(filter: PageFilter, paging: Pageable) =
        repository.findAll(courtFilterSpecification(filter), paging).map { it.courtDto }

    fun getById(id: Long): CourtDto =
        repository.findById(id).map { it.courtDto }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.courtDto }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun create(data: CourtDto) = repository.save(data.court).courtDto

    @Transactional
    fun update(data: CourtDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.court).courtDto
    }
}

val Court.courtDto get() = CourtDto(id = id, name = name, address = address)

val CourtDto.court get() = Court(id = id, name = name, address = address)

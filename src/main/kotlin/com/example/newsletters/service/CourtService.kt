package com.example.newsletters.service

import com.example.newsletters.dto.model.CourtDto
import com.example.newsletters.entity.Court
import com.example.newsletters.repository.CourtRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CourtService(val repository: CourtRepository) {
    fun getAll(): List<CourtDto> = repository.findAll().map { it.courtDto }
    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.courtDto }
    fun getByName(name: String) = repository.findByNameContainingIgnoreCase(name).map { it.courtDto }
    fun getByName(name: String, paging: Pageable) = repository.findByNameContainingIgnoreCase(name, paging).map { it.courtDto }
    fun getById(id: Long) = repository.findById(id).map { it.courtDto }.orElse(null)
    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.courtDto }
    fun delete(id: Long) = repository.deleteById(id)
    @Transactional
    fun create(data: CourtDto) = repository.save(data.court)
    @Transactional
    fun update(data: CourtDto) = repository.save(data.court)
}

val Court.courtDto get() = CourtDto(id = id, name = name, address = address)
val CourtDto.court get() = Court(id = id, name = name, address = address)

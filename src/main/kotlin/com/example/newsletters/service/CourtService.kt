package com.example.newsletters.service

import com.example.newsletters.dto.CourtDto
import com.example.newsletters.dto.RequestDestinationDto
import com.example.newsletters.entity.Court
import com.example.newsletters.entity.RequestDestination
import com.example.newsletters.repository.CourtRepository
import com.example.newsletters.repository.RequestDestinationRepository
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
    fun create(data: CourtDto) = repository.save(data.court)
    fun update(data: CourtDto) = repository.save(data.court)
}

val Court.courtDto get() = CourtDto(id = id, name = name, address = address)
val CourtDto.court get() = Court(id = id, name = name, address = address)

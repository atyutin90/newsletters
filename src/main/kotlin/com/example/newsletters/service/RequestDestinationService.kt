package com.example.newsletters.service

import com.example.newsletters.dto.RequestDestinationDto
import com.example.newsletters.entity.RequestDestination
import com.example.newsletters.repository.RequestDestinationRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RequestDestinationService(val repository: RequestDestinationRepository) {
    fun getAll(): List<RequestDestinationDto> = repository.findAll().map { it.requestDestinationDTO }
    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.requestDestinationDTO }
    fun getByName(name: String) = repository.findByNameContainingIgnoreCase(name).map { it.requestDestinationDTO }
    fun getByName(name: String, paging: Pageable) = repository.findByNameContainingIgnoreCase(name, paging).map { it.requestDestinationDTO }
    fun getById(id: Long) = repository.findById(id).map { it.requestDestinationDTO }.orElse(null)
    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.requestDestinationDTO }
    fun delete(id: Long) = repository.deleteById(id)
    fun create(data: RequestDestinationDto) = repository.save(data.requestDestination)
    fun update(data: RequestDestinationDto) = repository.save(data.requestDestination)
}

val RequestDestination.requestDestinationDTO get() = RequestDestinationDto(id = id, name = name, address = address)
val RequestDestinationDto.requestDestination get() = RequestDestination(id = id, name = name, address = address)

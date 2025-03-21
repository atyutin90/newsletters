package com.example.newsletters.service

import com.example.newsletters.dto.RequestDto
import com.example.newsletters.entity.Request
import com.example.newsletters.entity.RequestDestination
import com.example.newsletters.repository.RequestRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RequestStorageService(private val requestRepository: RequestRepository) {
    fun getAll(): List<RequestDto> = requestRepository.findAll().map { it.requestDTO }
    fun getByDebtorId(debtorId: Long): List<RequestDto> = requestRepository.findByDebtorId(debtorId).map { it.requestDTO }
    fun getAll(paging: Pageable) = requestRepository.findAll(paging).map { it.requestDTO }
    fun getById(id: Long) = requestRepository.findById(id).map { it.requestDTO }.orElse(null)
    fun getByIds(ids: List<Long>) = requestRepository.findAllById(ids).map { it.requestDTO }
    fun delete(id: Long) = requestRepository.deleteById(id)
    @Transactional
    fun create(request: RequestDto) = requestRepository.save(request.request)
    @Transactional
    fun update(request: RequestDto) = requestRepository.save(request.request)
}

private val Request.requestDTO: RequestDto get() = RequestDto(
    id = id,
    debtorId = debtorId,
    destinationId = requestDestination?.id,
    date = date,
    dateFrom = dateFrom,
    dateTo = dateTo,
    address = address
)

private val RequestDto.request: Request get() = Request(
    id = id,
    debtorId = debtorId ?: 0L,
    date = date,
    dateFrom = dateFrom,
    dateTo = dateTo,
    requestDestination = destinationId?.let { RequestDestination(destinationId) },
    address = address
)

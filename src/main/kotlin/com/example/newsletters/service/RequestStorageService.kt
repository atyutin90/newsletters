package com.example.newsletters.service

import com.example.newsletters.dto.model.RequestDto
import com.example.newsletters.entity.Request
import com.example.newsletters.entity.RequestDestination
import com.example.newsletters.repository.RequestRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RequestStorageService(
    private val repository: RequestRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<RequestDto> =
        repository.findAll()
            .map { it.requestDTO }

    fun getByDebtorId(debtorId: Long): List<RequestDto> =
        repository.findByDebtorId(debtorId)
            .map { it.requestDTO }

    fun getAll(paging: Pageable) =
        repository.findAll(paging)
            .map { it.requestDTO }

    fun getById(id: Long): RequestDto =
        repository.findById(id)
            .map { it.requestDTO }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.requestDTO }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun create(data: RequestDto) = repository.save(data.request).requestDTO

    @Transactional
    fun update(data: RequestDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.request).requestDTO
    }
}

private val Request.requestDTO: RequestDto
    get() = RequestDto(
        id = id,
        debtorId = debtorId,
        destinationId = destination?.id,
        destinationDetail = destinationDetail,
        date = date,
        dateFrom = dateFrom,
        dateTo = dateTo,
        address = address,
        accounts = accounts.joinToString(separator = "; ")
    )

private val RequestDto.request: Request
    get() = Request(
        id = id,
        debtorId = debtorId,
        destinationDetail = if (destinationId == 1L) destinationDetail else null,
        date = date,
        dateFrom = dateFrom,
        dateTo = dateTo,
        destination = destinationId?.let { RequestDestination(destinationId) },
        address = address,
        accounts = accounts?.split(";", ",")
            ?.map { it.trim() }
            ?.filterNot { it.isEmpty() }
            ?.toSet() ?: emptySet()
    )

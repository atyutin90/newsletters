package com.example.newsletters.service

import com.example.newsletters.dto.filter.specification.RequestDestinationSpecification.Companion.requestDestinationFilterSpecification
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.dto.model.RequestDestinationDto
import com.example.newsletters.entity.RequestDestination
import com.example.newsletters.repository.RequestDestinationRepository
import org.apache.commons.lang3.StringUtils.EMPTY
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Locale.getDefault

@Service
class RequestDestinationService(
    private val repository: RequestDestinationRepository,
    private val messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<RequestDestinationDto> =
        repository.findAllByOrderByNameAsc().asSequence()
            .map { it.requestDestinationDTO }
            .map {
                it.copy(
                    name = "${it.name}${
                        if (it.enabled == true) EMPTY
                        else messageSource.getMessage("disabled-value", arrayOf(), getDefault())
                    }"
                )
            }
            .toList()

    fun getAll(paging: Pageable) =
        repository.findAll(paging)
            .map { it.requestDestinationDTO }

    fun getAll(filter: PageFilter, paging: Pageable) =
        repository.findAll(requestDestinationFilterSpecification(filter), paging)
            .map { it.requestDestinationDTO }

    fun getById(id: Long): RequestDestinationDto =
        repository.findById(id).map { it.requestDestinationDTO }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) =
        repository.findAllById(ids)
            .map { it.requestDestinationDTO }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun save(data: RequestDestinationDto) =
        repository.save(data.requestDestination).requestDestinationDTO
}

val RequestDestination.requestDestinationDTO
    get() = RequestDestinationDto(
        id = id,
        name = name,
        enabled = enabled,
        documentTemplateId = documentTemplateId
    )

val RequestDestinationDto.requestDestination
    get() = RequestDestination(
        id = id,
        name = name,
        enabled = enabled ?: true,
        documentTemplateId = documentTemplateId
    )

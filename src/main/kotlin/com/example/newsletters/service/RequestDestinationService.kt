package com.example.newsletters.service

import com.example.newsletters.dto.model.RequestDestinationDto
import com.example.newsletters.entity.RequestDestination
import com.example.newsletters.repository.RequestDestinationRepository
import jakarta.transaction.Transactional
import org.apache.commons.lang3.StringUtils.EMPTY
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Locale.getDefault

@Service
class RequestDestinationService(
    private val repository: RequestDestinationRepository,
    private val messageSource: MessageSource
) {
    fun getAll(): List<RequestDestinationDto> = repository.findAllByOrderByNameAsc().asSequence()
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

    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.requestDestinationDTO }
    fun getByName(name: String) = repository.findByNameContainingIgnoreCase(name).map { it.requestDestinationDTO }
    fun getByName(name: String, paging: Pageable) =
        repository.findByNameContainingIgnoreCase(name, paging).map { it.requestDestinationDTO }

    fun getById(id: Long) = repository.findById(id).map { it.requestDestinationDTO }.orElse(null)
    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.requestDestinationDTO }
    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun save(data: RequestDestinationDto) = repository.save(data.requestDestination)
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

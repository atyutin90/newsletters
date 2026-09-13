package com.example.newsletters.service

import com.example.newsletters.dto.model.PublicationDto
import com.example.newsletters.entity.Publication
import com.example.newsletters.repository.PublicationRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PublicationStorageService(
    private val repository: PublicationRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<PublicationDto> =
        repository.findAll()
            .map { it.publicationDto }

    fun getByDebtorId(debtorId: Long): List<PublicationDto> =
        repository.findByDebtorId(debtorId)
            .map { it.publicationDto }

    fun getAll(paging: Pageable) =
        repository.findAll(paging)
            .map { it.publicationDto }

    fun getById(id: Long): PublicationDto =
        repository.findById(id)
            .map { it.publicationDto }
            .orElseThrow { notExist(id) }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun create(data: PublicationDto) =
        repository.save(data.publication).publicationDto

    @Transactional
    fun update(data: PublicationDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.publication).publicationDto
    }
}

val Publication.publicationDto: PublicationDto get() = PublicationDto(
    id = this.id,
    debtorId = this.debtorId,
    publicationDate = this.publicationDate,
    appointmentDate = this.appointmentDate,
    appointmentTime = this.appointmentTime,
    hallNumber = this.hallNumber,
    kommersantMessageNumber = this.kommersantMessageNumber,
    efrsbMessageNumber = this.efrsbMessageNumber,
    kommersantIssueNumber = this.kommersantIssueNumber,
    additionInformation = this.additionInformation,
)

val PublicationDto.publication: Publication get() = Publication(
    id = id,
    debtorId = this.debtorId,
    publicationDate = this.publicationDate,
    appointmentDate = this.appointmentDate,
    appointmentTime = this.appointmentTime,
    hallNumber = this.hallNumber,
    kommersantMessageNumber = this.kommersantMessageNumber,
    efrsbMessageNumber = this.efrsbMessageNumber,
    kommersantIssueNumber = this.kommersantIssueNumber,
    additionInformation = this.additionInformation,
)

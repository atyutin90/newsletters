package com.example.newsletters.service

import com.example.newsletters.dto.model.PublicationDto
import com.example.newsletters.entity.Publication
import com.example.newsletters.repository.PublicationRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PublicationStorageService(private val publicationRepository: PublicationRepository) {
    fun getAll(): List<PublicationDto> = publicationRepository.findAll().map { it.publicationDto }
    fun getByDebtorId(debtorId: Long): List<PublicationDto> = publicationRepository.findByDebtorId(debtorId).map { it.publicationDto }
    fun getAll(paging: Pageable) = publicationRepository.findAll(paging).map { it.publicationDto }
    fun getById(id: Long) = publicationRepository.findById(id).map { it.publicationDto }.orElse(null)
    fun delete(id: Long) = publicationRepository.deleteById(id)

    @Transactional
    fun create(request: PublicationDto) = publicationRepository.save(request.publication)

    @Transactional
    fun update(request: PublicationDto) = publicationRepository.save(request.publication)
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
    debtorId = this.debtorId ?: 0L,
    publicationDate = this.publicationDate,
    appointmentDate = this.appointmentDate,
    appointmentTime = this.appointmentTime,
    hallNumber = this.hallNumber,
    kommersantMessageNumber = this.kommersantMessageNumber,
    efrsbMessageNumber = this.efrsbMessageNumber,
    kommersantIssueNumber = this.kommersantIssueNumber,
    additionInformation = this.additionInformation,
)

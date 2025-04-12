package com.example.newsletters.service

import com.example.newsletters.dto.DebtorMeetingParticipantDto
import com.example.newsletters.entity.DebtorMeetingParticipant
import com.example.newsletters.repository.DebtorMeetingParticipantRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DebtorMeetingParticipantStorageService(private val repository: DebtorMeetingParticipantRepository) {

    fun getAll(): List<DebtorMeetingParticipantDto> = repository.findAll().map { it.debtorMeetingParticipantDTO }

    fun getByDebtorMeetingId(debtorMeetingId: Long): List<DebtorMeetingParticipantDto> =
        repository.findByDebtorMeetingId(debtorMeetingId).map { it.debtorMeetingParticipantDTO }

    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.debtorMeetingParticipantDTO }

    fun getById(id: Long) = repository.findById(id).map { it.debtorMeetingParticipantDTO }.orElse(null)

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.debtorMeetingParticipantDTO }

    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun create(request: DebtorMeetingParticipantDto) = repository.save(request.debtorMeetingParticipant)

    @Transactional
    fun update(request: DebtorMeetingParticipantDto) = repository.save(request.debtorMeetingParticipant)
}

val DebtorMeetingParticipant.debtorMeetingParticipantDTO
    get() = DebtorMeetingParticipantDto(
        id = this.id,
        debtorMeetingId = this.debtorMeetingId,
        name = this.name,
        address = this.address,
    )

val DebtorMeetingParticipantDto.debtorMeetingParticipant
    get() = DebtorMeetingParticipant(
        id = this.id,
        debtorMeetingId = this.debtorMeetingId,
        name = this.name,
        address = this.address,
    )

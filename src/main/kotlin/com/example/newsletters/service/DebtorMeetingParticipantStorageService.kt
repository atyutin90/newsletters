package com.example.newsletters.service

import com.example.newsletters.dto.DebtorMeetingParticipantDto
import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.DebtorMeetingParticipant
import com.example.newsletters.entity.MeetingParticipant
import com.example.newsletters.entity.enum.MeetingParticipantType.DEBTOR
import com.example.newsletters.entity.enum.MeetingParticipantType.CREDITOR
import com.example.newsletters.entity.enum.MeetingParticipantType.PARTICIPANT
import com.example.newsletters.entity.enum.MeetingParticipantType.Companion.meetingParticipantTypeOf
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorMeetingParticipantRepository
import com.example.newsletters.repository.MeetingParticipantRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DebtorMeetingParticipantStorageService(
    private val debtorMeetingParticipantRepository: DebtorMeetingParticipantRepository,
    private val creditorRepository: CreditorRepository,
    private val meetingParticipantRepository: MeetingParticipantRepository,
) {

    fun getAll(): List<DebtorMeetingParticipantDto> = debtorMeetingParticipantRepository.findAll().map { dtoOf(it) }

    fun getByDebtorMeetingId(debtorMeetingId: Long): List<DebtorMeetingParticipantDto> =
        debtorMeetingParticipantRepository.findByDebtorMeetingId(debtorMeetingId).map { dtoOf(it) }

    fun getAll(paging: Pageable): Page<DebtorMeetingParticipantDto?>? =
        debtorMeetingParticipantRepository.findAll(paging).map { dtoOf(it) }

    fun getById(id: Long): DebtorMeetingParticipantDto? =
        debtorMeetingParticipantRepository.findById(id).map { dtoOf(it) }.orElse(null)

    fun getByIds(ids: List<Long>) = debtorMeetingParticipantRepository.findAllById(ids).map { dtoOf(it) }

    @Transactional
    fun delete(id: Long) {
        debtorMeetingParticipantRepository.deleteById(id)
    }

    @Transactional
    fun create(request: DebtorMeetingParticipantDto): DebtorMeetingParticipant? =
        debtorMeetingParticipantRepository.save(request.debtorMeetingParticipant())

    @Transactional
    fun update(request: DebtorMeetingParticipantDto): DebtorMeetingParticipant? =
        debtorMeetingParticipantRepository.save(request.debtorMeetingParticipant())

    private fun dtoOf(data: DebtorMeetingParticipant): DebtorMeetingParticipantDto = run {
        var address: String? = null
        var name: String? = null
        if (data.type == PARTICIPANT) {
            data.meetingParticipant?.let {
                address = it.address
                name = it.name
            }
        }
        DebtorMeetingParticipantDto(
            id = data.id,
            debtorMeetingId = data.debtorMeetingId,
            type = data.type?.name,
            withoutRight = data.withoutRight,
            creditorId = data.creditorId,
            address = address,
            name = name
        )
    }

    private fun DebtorMeetingParticipantDto.debtorMeetingParticipant(): DebtorMeetingParticipant =
        when (meetingParticipantTypeOf(this.type)) {
            DEBTOR -> createDebtorParticipant(this)
            CREDITOR -> createCreditorParticipant(this)
            PARTICIPANT -> createNewParticipant(this)
            else -> throw IllegalArgumentException("Unknown MeetingParticipantType: ${this.type}")
        }

    private fun createNewParticipant(data: DebtorMeetingParticipantDto): DebtorMeetingParticipant = run {
        val originalDebtorMeetingParticipant =
            data.id?.let { id -> debtorMeetingParticipantRepository.findById(id).orElse(null) }
        if (originalDebtorMeetingParticipant == null) {
            DebtorMeetingParticipant(
                id = data.id,
                debtorMeetingId = data.debtorMeetingId,
                type = meetingParticipantTypeOf(data.type),
                creditor = null,
                meetingParticipant = MeetingParticipant(name = data.name, address = data.address),
                withoutRight = data.withoutRight,
            )
        } else {
            val meetingParticipant = originalDebtorMeetingParticipant.meetingParticipant
                ?.let { mp ->
                    if (!mp.name.equals(data.name) || !mp.address.equals(data.address)) {
                        mp.apply { this.name = data.name; this.address = data.address }
                    } else {
                        mp
                    }
                } ?: MeetingParticipant(name = data.name, address = data.address)

            DebtorMeetingParticipant(
                id = data.id,
                debtorMeetingId = data.debtorMeetingId,
                type = meetingParticipantTypeOf(data.type),
                creditor = null,
                meetingParticipant = meetingParticipant,
                withoutRight = data.withoutRight,
            )
        }
    }

    private fun createDebtorParticipant(data: DebtorMeetingParticipantDto): DebtorMeetingParticipant = run {
        deleteMeetingParticipant(data)
        DebtorMeetingParticipant(
            id = data.id,
            debtorMeetingId = data.debtorMeetingId,
            type = meetingParticipantTypeOf(data.type),
            creditor = null,
            meetingParticipant = null,
            withoutRight = data.withoutRight,
        )
    }

    private fun createCreditorParticipant(data: DebtorMeetingParticipantDto): DebtorMeetingParticipant =
        if (creditorRepository.existsById(requireNotNull(data.creditorId))) {
            deleteMeetingParticipant(data)
            DebtorMeetingParticipant(
                id = data.id,
                debtorMeetingId = data.debtorMeetingId,
                type = meetingParticipantTypeOf(data.type),
                creditor = Creditor(id = data.creditorId),
                meetingParticipant = null,
                withoutRight = data.withoutRight,
            )
        } else {
            throw EntityNotFoundException("Creditor with id ${data.creditorId} not found")
        }

    private fun deleteMeetingParticipant(data: DebtorMeetingParticipantDto) {
        data.id?.let { id ->
            val originalDebtorMeetingParticipant = debtorMeetingParticipantRepository.findById(id).orElse(null)
            originalDebtorMeetingParticipant?.meetingParticipant?.let { mp ->
                meetingParticipantRepository.delete(mp)
            }
        }
    }
}

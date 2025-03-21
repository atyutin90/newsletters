package com.example.newsletters.service

import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.enum.ClientType
import com.example.newsletters.repository.CreditorRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CreditorStorageService(private val creditorRepository: CreditorRepository) {
    fun getAll(): List<CreditorDto> = creditorRepository.findAll().map { it.creditorDTO }
    fun getAll(paging: Pageable) = creditorRepository.findAll(paging).map { it.creditorDTO }
    fun getByDebtorId(debtorId: Long): List<CreditorDto> = creditorRepository.findByDebtorId(debtorId).map { it.creditorDTO }
    fun getById(id: Long) = creditorRepository.findById(id).map { it.creditorDTO }.orElse(null)
    fun getByIds(ids: List<Long>) = creditorRepository.findAllById(ids).map { it.creditorDTO }
    fun delete(id: Long) = creditorRepository.deleteById(id)
    @Transactional
    fun create(creditor: CreditorDto) = creditorRepository.save(creditor.creditor)
    @Transactional
    fun update(creditor: CreditorDto) = creditorRepository.save(creditor.creditor)
}

val Creditor.creditorDTO get() = CreditorDto(
    id = id,
    debtorId = debtorId,
    clientType = clientType?.name,
    name = name,
    taxpayerIdentificationNumber = taxpayerIdentificationNumber,
    primaryStateRegistrationNumber = primaryStateRegistrationNumber,
    address = address,
    passportSerial = passportSerial?.toString(),
    passportNumber = passportNumber?.toString(),
    principalAmount = principalAmount,
    stateDutyAmount = stateDutyAmount,
    executionWrit = executionWrit,
    executionDate = executionDate
)

val CreditorDto.creditor get() = Creditor(
    id = id,
    debtorId = debtorId,
    clientType = ClientType.clientTypeOf(clientType),
    name = name,
    taxpayerIdentificationNumber = taxpayerIdentificationNumber,
    primaryStateRegistrationNumber = primaryStateRegistrationNumber,
    address = address,
    passportSerial = passportSerial?.toIntOrNull(),
    passportNumber = passportNumber?.toIntOrNull(),
    principalAmount = principalAmount,
    stateDutyAmount = stateDutyAmount,
    executionWrit = executionWrit,
    executionDate = executionDate
)

package com.example.newsletters.service

import com.example.newsletters.dto.model.CreditorDto
import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.enum.ClientType
import com.example.newsletters.entity.enum.ClientType.Companion.clientTypeOf
import com.example.newsletters.repository.CreditorRepository
import jakarta.transaction.Transactional
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CreditorStorageService(
    private val repository: CreditorRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<CreditorDto> = repository.findAll().map { it.creditorDTO }

    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.creditorDTO }

    fun getByDebtorId(debtorId: Long): List<CreditorDto> =
        repository.findByDebtorId(debtorId).map { it.creditorDTO }

    fun getById(id: Long): CreditorDto =
        repository.findById(id)
            .map { it.creditorDTO }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.creditorDTO }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun create(creditor: CreditorDto) = repository.save(creditor.creditor).creditorDTO

    @Transactional
    fun update(data: CreditorDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.creditor).creditorDTO
    }
}

val Creditor.creditorDTO
    get() = CreditorDto(
        id = id,
        debtorId = debtorId,
        clientType = clientType?.name,
        name = name,
        taxpayerIdentificationNumber = taxpayerIdentificationNumber,
        primaryStateRegistrationNumber = primaryStateRegistrationNumber,
        address = address,
        passportSerial = passportSerial?.toString(),
        passportNumber = passportNumber?.toString(),
        executionWrit = executionWrit,
        executionDate = executionDate
    )

val CreditorDto.creditor
    get() = run {
        val clientType = clientTypeOf(clientType)
        Creditor(
            id = id,
            debtorId = debtorId,
            clientType = clientType,
            name = name,
            taxpayerIdentificationNumber = taxpayerIdentificationNumber,
            primaryStateRegistrationNumber = if (clientType == ClientType.LEGAL) primaryStateRegistrationNumber else null,
            address = address,
            passportSerial = if (clientType == ClientType.INDIVIDUAL) passportSerial?.toIntOrNull() else null,
            passportNumber = if (clientType == ClientType.INDIVIDUAL) passportNumber?.toIntOrNull() else null,
            executionWrit = executionWrit,
            executionDate = executionDate
        )
    }

package com.example.newsletters.service

import com.example.newsletters.dto.filter.specification.DebtorSpecification.Companion.debtorFilterSpecification
import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.entity.Debtor
import com.example.newsletters.repository.DebtorRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DebtorStorageService(
    private val debtorRepository: DebtorRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<DebtorDto> =
        debtorRepository.findAll()
            .map { it.debtorDTO }

    fun getAll(paging: Pageable) =
        debtorRepository.findAll(paging)
            .map { it.debtorDTO }

    fun getAll(filter: PageFilter, paging: Pageable) =
        debtorRepository.findAll(debtorFilterSpecification(filter), paging)
            .map { it.debtorDTO }

    fun getByArbitrationManagerId(arbitrationManagerId: Long): List<DebtorDto> =
        debtorRepository.findByArbitrationManagerId(arbitrationManagerId)
            .map { it.debtorDTO }

    fun getById(id: Long): DebtorDto =
        debtorRepository.findById(id)
            .map { it.debtorDTO }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) =
        debtorRepository.findAllById(ids)
            .map { it.debtorDTO }

    @Transactional
    fun delete(id: Long) = run {
        val result = debtorRepository.existsById(id)
        if (!result) notExist(id)
        debtorRepository.deleteById(id)
    }

    @Transactional
    fun create(debtor: DebtorDto) = debtorRepository.save(debtor.debtor).debtorDTO

    @Transactional
    fun update(data: DebtorDto): DebtorDto = run {
        val result = data.id?.let { debtorRepository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        debtorRepository.save(data.debtor).debtorDTO
    }
}

val Debtor.debtorDTO
    get() =
        DebtorDto(
            id = id,
            arbitrationManagerId = arbitrationManagerId,
            fullName = fullName,
            name = name,
            caseNumber = caseNumber,
            address = address,
            courtAct = courtAct,
            actDate = actDate,
            resolutionDate = resolutionDate,
            taxRegistrationReasonCode = taxRegistrationReasonCode,
            taxpayerIdentificationNumber = taxpayerIdentificationNumber,
            primaryStateRegistrationNumber = primaryStateRegistrationNumber,
            registryDate = registryDate,
            registryClosingDate = registryClosingDate,
            arbitrationCase = arbitrationCase,
            documentNumber = documentNumber
        )
val DebtorDto.debtor
    get() = Debtor(
        id = id,
        arbitrationManagerId = arbitrationManagerId,
        fullName = fullName,
        name = name,
        caseNumber = caseNumber,
        address = address,
        courtAct = courtAct,
        actDate = actDate,
        resolutionDate = resolutionDate,
        taxRegistrationReasonCode = taxRegistrationReasonCode,
        taxpayerIdentificationNumber = taxpayerIdentificationNumber,
        primaryStateRegistrationNumber = primaryStateRegistrationNumber,
        registryDate = registryDate,
        registryClosingDate = registryClosingDate,
        arbitrationCase = arbitrationCase,
        documentNumber = documentNumber
    )

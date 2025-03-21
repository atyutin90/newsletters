package com.example.newsletters.service

import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.entity.Debtor
import com.example.newsletters.repository.DebtorRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DebtorStorageService(private val debtorRepository: DebtorRepository) {
    fun getAll(): List<DebtorDto> = debtorRepository.findAll().map { it.debtorDTO }
    fun getAll(paging: Pageable) = debtorRepository.findAll(paging).map { it.debtorDTO }
    fun getByArbitrationManagerId(arbitrationManagerId: Long): List<DebtorDto> =
        debtorRepository.findByArbitrationManagerId(arbitrationManagerId).map { it.debtorDTO }

    fun getByName(name: String) = debtorRepository.findByNameContainingIgnoreCase(name).map { it.debtorDTO }
    fun getByName(name: String, paging: Pageable) =
        debtorRepository.findByNameContainingIgnoreCase(name, paging).map { it.debtorDTO }

    fun getById(id: Long) = debtorRepository.findById(id).map { it.debtorDTO }.orElse(null)
    fun getByIds(ids: List<Long>) = debtorRepository.findAllById(ids).map { it.debtorDTO }
    fun delete(id: Long) = debtorRepository.deleteById(id)

    @Transactional
    fun create(debtor: DebtorDto) = debtorRepository.save(debtor.debtor)

    @Transactional
    fun update(debtor: DebtorDto) = run {
        if (debtor.id != null) {
            debtorRepository.findById(debtor.id)
                .map {
                    it.apply {
                        id = debtor.id
                        arbitrationManagerId = debtor.arbitrationManagerId
                        fullName = debtor.fullName
                        name = debtor.name
                        caseNumber = debtor.caseNumber
                        address = debtor.address
                        courtAct = debtor.courtAct
                        actDate = debtor.actDate
                        resolutionDate = debtor.resolutionDate
                        taxRegistrationReasonCode = debtor.taxRegistrationReasonCode
                        taxpayerIdentificationNumber = debtor.taxpayerIdentificationNumber
                        primaryStateRegistrationNumber = debtor.primaryStateRegistrationNumber
                        registryDate = debtor.registryDate
                        registryClosingDate = debtor.registryClosingDate
                    }
                }
                .map { debtorRepository.save(it) }
        }
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
            arbitrationCase = arbitrationCase
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
        arbitrationCase = arbitrationCase
    )

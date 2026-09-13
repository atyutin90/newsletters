package com.example.newsletters.service

import com.example.newsletters.dto.filter.specification.ArbitrationManagerSpecification.Companion.userFilterSpecification
import com.example.newsletters.dto.model.ArbitrationManagerDto
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.entity.ArbitrationManager
import com.example.newsletters.repository.ArbitrationManagerRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ArbitrationManagerService(
    private val repository: ArbitrationManagerRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<ArbitrationManagerDto> =
        repository.findAll()
            .map { it.arbitrationManagerDto }

    fun getAll(filter: PageFilter, paging: Pageable) =
        repository.findAll(userFilterSpecification(filter), paging)
            .map { it.arbitrationManagerDto }

    fun getById(id: Long): ArbitrationManagerDto =
        repository.findById(id)
            .map { it.arbitrationManagerDto }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) =
        repository.findAllById(ids)
            .map { it.arbitrationManagerDto }

    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    fun create(data: ArbitrationManagerDto) =
        repository.save(data.arbitrationManager).arbitrationManagerDto

    fun update(data: ArbitrationManagerDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.arbitrationManager).arbitrationManagerDto
    }
}

val ArbitrationManager.arbitrationManagerDto
    get() = ArbitrationManagerDto(
        id = id,
        fullName = fullName,
        taxpayerIdentificationNumber = taxpayerIdentificationNumber,
        personalInsurancePolicyNumber = personalInsurancePolicyNumber,
        postalAddress = postalAddress,
        actualPostalAddress = actualPostalAddress,
        email = email,
        phone = phone,
        sroData = sroData,
        sroRegistrationNumber = sroRegistrationNumber,
        insuranceCompanyName = insuranceCompanyName,
        insuranceContractNumber = insuranceContractNumber,
        insuranceContractTerm = insuranceContractTerm,
    )

val ArbitrationManagerDto.arbitrationManager
    get() = ArbitrationManager(
        id = id,
        fullName = fullName,
        taxpayerIdentificationNumber = taxpayerIdentificationNumber,
        personalInsurancePolicyNumber = personalInsurancePolicyNumber,
        postalAddress = postalAddress,
        actualPostalAddress = actualPostalAddress,
        email = email,
        phone = phone,
        sroData = sroData,
        sroRegistrationNumber = sroRegistrationNumber,
        insuranceCompanyName = insuranceCompanyName,
        insuranceContractNumber = insuranceContractNumber,
        insuranceContractTerm = insuranceContractTerm,
    )

package com.example.newsletters.service

import com.example.newsletters.dto.ArbitrationManagerDto
import com.example.newsletters.entity.ArbitrationManager
import com.example.newsletters.repository.ArbitrationManagerRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ArbitrationManagerService(val repository: ArbitrationManagerRepository) {
    fun getAll(): List<ArbitrationManagerDto> = repository.findAll().map { it.arbitrationManagerDto }
    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.arbitrationManagerDto }
    fun getByFullName(name: String) = repository.findByFullNameContainingIgnoreCase(name).map { it.arbitrationManagerDto }
    fun getByFullName(name: String, paging: Pageable) = repository.findByFullNameContainingIgnoreCase(name, paging).map { it.arbitrationManagerDto }
    fun getById(id: Long): ArbitrationManagerDto = repository.findById(id).map { it.arbitrationManagerDto }.orElse(null)
    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.arbitrationManagerDto }
    fun delete(id: Long) = repository.deleteById(id)
    fun create(data: ArbitrationManagerDto) = repository.save(data.arbitrationManager)
    fun update(data: ArbitrationManagerDto) = repository.save(data.arbitrationManager)
}

val ArbitrationManager.arbitrationManagerDto get() = ArbitrationManagerDto(
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

val ArbitrationManagerDto.arbitrationManager get() = ArbitrationManager(
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

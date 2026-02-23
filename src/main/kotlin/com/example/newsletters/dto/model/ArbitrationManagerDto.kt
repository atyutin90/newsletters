package com.example.newsletters.dto.model

data class ArbitrationManagerDto(
    val id: Long? = null,
    val fullName: String? = null,
    val taxpayerIdentificationNumber: String? = null,
    val personalInsurancePolicyNumber: String? = null,
    val postalAddress: String? = null,
    val actualPostalAddress: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val sroData: String? = null,
    val sroRegistrationNumber: String? = null,
    val insuranceCompanyName: String? = null,
    val insuranceContractNumber: String? = null,
    val insuranceContractTerm: String? = null
)

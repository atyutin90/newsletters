package com.example.newsletters.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(name = "arbitration_manager")
class ArbitrationManager(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime?,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime?,

    @Column(name = "full_name", length = 300)
    val fullName: String,

    @Column(name = "taxpayer_identification_number", length = 50)
    var taxpayerIdentificationNumber: String? = null,

    @Column(name = "personal_insurance_policy_number", length = 50)
    var personalInsurancePolicyNumber: String? = null,

    @Column(name = "postal_addr", length = 50)
    var postalAddress: String? = null,

    @Column(name = "actual_postal_addr", length = 50)
    var actualPostalAddress: String? = null,

    @Column(name = "email", length = 100)
    var email: String? = null,

    @Column(name = "phone", length = 50)
    var phone: String? = null,

    @Column(name = "sro_data", length = 300)
    var sroData: String? = null,

    @Column(name = "sro_registration_number", length = 50)
    var sroRegistrationNumber: String? = null,

    @Column(name = "insurance_company_name", length = 100)
    var insuranceCompanyName: String? = null,

    @Column(name = "insurance_contract_number", length = 100)
    var insuranceContractNumber: String? = null,

    @Column(name = "insurance_contract_term", length = 100)
    var insuranceContractTerm: String? = null,
) : BaseEntity

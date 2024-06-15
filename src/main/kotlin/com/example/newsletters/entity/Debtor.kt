package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.ZonedDateTime

@Entity
@Table(name = "debtor")
class Debtor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(
        name = "arbitration_manager_id",
        nullable = true,
    )
    var arbitrationManagerId: Long? = null,

    @Column(name = "name", nullable = false, length = 256)
    var name: String? = null,

    @Column(name = "full_name", length = 256)
    var fullName: String? = null,

    @Column(name = "case_number", length = 256)
    var caseNumber: String? = null,

    @Column(name = "address", length = 256)
    var address: String? = null,

    @Column(name = "court_act", length = 256)
    var courtAct: String? = null,

    @Column(name = "act_date")
    var actDate: LocalDate? = null,

    @Column(name = "resolution_date")
    var resolutionDate: LocalDate? = null,

    @Column(name = "tax_registration_reason_code", length = 50)
    var taxRegistrationReasonCode: String? = null,

    @Column(name = "taxpayer_identification_number", length = 50)
    var taxpayerIdentificationNumber: String? = null,

    @Column(name = "primary_state_registration_number", length = 50)
    var primaryStateRegistrationNumber: String? = null,

    @Column(name = "registry_date")
    var registryDate: LocalDate? = null,

    @Column(name = "registry_closing_date")
    var registryClosingDate: LocalDate? = null,

    @Column(name = "arbitration_case", length = 300)
    var arbitrationCase: String? = null,

    @Column(name = "document_number", length = 20)
    var documentNumber: String? = null,

) : BaseEntity

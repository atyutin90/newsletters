package com.example.newsletters.entity

import com.example.newsletters.entity.enum.ClientType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime

@Entity
@Table(name = "creditor")
class Creditor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "debtor_id", nullable = false)
    var debtorId : Long? = null,

    @Column(name = "client_type", length = 50)
    var clientType: ClientType? = null,

    @Column(name = "name", length = 256)
    var name: String? = null,

    @Column(name = "taxpayer_identification_number", length = 50)
    var taxpayerIdentificationNumber: String? = null,

    @Column(name = "primary_state_registration_number", length = 50)
    var primaryStateRegistrationNumber: String? = null,

    @Column(name = "passport_serial", length = 10)
    var passportSerial: Int? = null,

    @Column(name = "passport_number", length = 10)
    var passportNumber: Int? = null,

    @Column(name = "address", length = 500)
    var address: String? = null,

    @Column(name = "execution_writ", length = 100)
    var executionWrit : String? = null,

    @Column(name = "execution_date")
    var executionDate : LocalDate? = null
) : BaseEntity

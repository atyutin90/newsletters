package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

@Entity
@Table(name = "debtor_meeting")
class DebtorMeeting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime?,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime?,

    @Column(name = "debtor_id")
    var debtorId: Long? = null,

    @Column(name = "date")
    var date: LocalDate?,

    @Column(name = "time")
    var time: LocalTime? = null,

    @Column(name = "registration_time_from")
    var registrationTimeFrom: LocalTime? = null,

    @Column(name = "registration_time_to")
    var registrationTimeTo: LocalTime? = null,

    @Column(name = "address",  nullable = false, length = 500)
    var address: String,

    @Column(name = "familiarization_time_from",  nullable = false, length = 500)
    var familiarizationTimeFrom: ZonedDateTime? = null,

    @Column(name = "familiarization_time_to",  nullable = false, length = 500)
    var familiarizationTimeTo: ZonedDateTime? = null,

) : BaseEntity

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
@Table(name = "worker_meeting")
class WorkerMeeting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "debtor_id", nullable = true,)
    var debtorId: Long,

    @Column(name = "date")
    var date: LocalDate? = null,

    @Column(name = "time")
    var time: LocalTime? = null,

    @Column(name = "registration_time_from")
    var registrationTimeFrom: LocalTime? = null,

    @Column(name = "registration_time_to")
    var registrationTimeTo: LocalTime? = null,

    @Column(name = "address", length = 500)
    var address: String? = null,

    @Column(name = "topic", length = 500)
    var topic: String? = null,

) : BaseEntity

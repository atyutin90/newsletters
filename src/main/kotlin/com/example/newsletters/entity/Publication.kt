package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.util.TypeInformation
import org.thymeleaf.standard.expression.AdditionExpression
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

@Entity
@Table(name = "publication")
class Publication(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "publication_date")
    var publicationDate: LocalDate? = null,

    @Column(name = "appointment_date")
    var appointmentDate: LocalDate? = null,

    @Column(name = "appointment_time")
    var appointmentTime: LocalTime? = null,

    @Column(name = "hall_number", length = 10)
    var hallNumber: String? = null,

    @Column(name = "kommersant_message_number", length = 256)
    var kommersantMessageNumber: String? = null,

    @Column(name = "efrsb_message_number", length = 256)
    var efrsbMessageNumber: String? = null,

    @Column(name = "kommersant_issue_number", length = 256)
    var kommersantIssueNumber: String? = null,

    @Column(
        name = "debtor_id",
        nullable = true,
    )
    var debtorId: Long,

    @Column(name = "add_info", length = 1000)
    var additionInformation: String? = null,

    ) : BaseEntity

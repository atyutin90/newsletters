package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
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
    var publicationDate: ZonedDateTime? = null,

    @Column(name = "appointment_time")
    var appointmentTime: String? = null,

    @ManyToOne
    @JoinColumn(name = "court_id", nullable = false)
    var court: Court? = null,

    @Column(name = "hall_number", length = 10)
    var hallNumber: String? = null,

    @Column(name = "kommersant_message_number", length = 256)
    var kommersantMessageNumber: String? = null,

    @Column(name = "efrsb_message_number", length = 256)
    var efrsbMessageNumber: String? = null,

    @Column(name = "kommersant_issue_number", length = 256)
    var kommersantIssueNumber: String? = null,

) : BaseEntity


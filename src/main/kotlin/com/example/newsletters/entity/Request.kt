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
import java.time.LocalDate
import java.time.ZonedDateTime

/**
 * Запрос.
 */
@Entity
@Table(name = "request")
class Request(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(
        name = "debtor_id",
        nullable = true,
    )
    var debtorId: Long,

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    val requestDestination: RequestDestination? = null,

    @Column(
        name = "date",
        nullable = true,
    )
    var date: LocalDate? = null,

    @Column(
        name = "date_from",
        nullable = true,
    )
    var dateFrom: LocalDate? = null,

    @Column(
        name = "date_to",
        nullable = true,
    )
    var dateTo: LocalDate? = null,

    @Column(name = "address", length = 500)
    var address: String? = null,

    ) : BaseEntity


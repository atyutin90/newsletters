package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(name = "court")
class Court(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime?,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime?,

    @Column(name = "name")
    var name: String,

    @Column(name = "address",  nullable = false, length = 500)
    var address: String,
) : BaseEntity
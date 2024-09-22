package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(name = "file")
class FileDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime?,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime?,

    var name: String? = null,
    var type: String? = null,
    @Lob
     var data: ByteArray? = null
) : BaseEntity {
    constructor(
        name: String? = null,
        type: String? = null,
        data: ByteArray? = null
    ) : this(null, null, null, name, type, data)
}
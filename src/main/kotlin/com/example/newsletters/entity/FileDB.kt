package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table

@Entity
@Table(name = "file")
class FileDB(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,
    var name: String? = null,
    var type: String? = null,
    @Lob
     var data: ByteArray? = null
) : BaseEntity() {
    constructor(
        name: String? = null,
        type: String? = null,
        data: ByteArray? = null
    ) : this(null, name, type, data)
}
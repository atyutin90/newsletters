package com.example.newsletters.entity

import com.example.newsletters.dto.TemplateType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table

@Entity
@Table(name = "template")
class Template(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,

    var name: String? = null,

    @Enumerated(EnumType.STRING)
    var type: TemplateType? = null,

    var contentType: String? = null,

    @Lob
     var data: ByteArray? = null
) : BaseEntity() {
    constructor(
        name: String? = null,
        type: TemplateType? = null,
        contentType: String? = null,
        data: ByteArray? = null
    ) : this(null, name, type, contentType, data)
}
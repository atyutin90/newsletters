package com.example.newsletters.entity

import com.example.newsletters.entity.enum.DocumentTemplateType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

/**
 * Шаблон документов
 */
@Entity
@Table(name = "document_template")
class DocumentTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "name", length = 255)
    var name: String? = null,

    @Column(name = "type", length = 40)
    var type: DocumentTemplateType? = null,

    @Column(name = "resource_path", length = 1000)
    var resourcePath: String? = null

) : BaseEntity

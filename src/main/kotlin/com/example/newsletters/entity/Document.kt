package com.example.newsletters.entity

import com.example.newsletters.dto.DocumentInfo
import com.example.newsletters.entity.enum.DocumentTemplateType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.type.SqlTypes
import java.time.ZonedDateTime

/**
 * Документ
 */
@Entity
@Table(name = "document")
class Document(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "debtor_id", nullable = true)
    var debtorId: Long,

    @Column(name = "type", length = 40)
    var type: DocumentTemplateType? = null,

    @Column(name = "name", length = 255)
    var name: String? = null,

    @Column(name = "template_resource_path", length = 1000)
    var templateResourcePath: String? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var info: DocumentInfo? = null

) : BaseEntity

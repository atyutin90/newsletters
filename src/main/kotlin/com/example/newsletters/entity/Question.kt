package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(name = "question")
class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "value", length = 500)
    var value: String? = null,

    @Column(name = "position")
    var position: Int? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "question_document_template",
        joinColumns = [JoinColumn(name = "question_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "document_template_id", referencedColumnName = "id")]
    )
    var documentTemplates: Set<DocumentTemplate> = setOf()

) : BaseEntity

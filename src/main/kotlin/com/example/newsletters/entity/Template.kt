package com.example.newsletters.entity
import com.example.newsletters.entity.enum.DocumentTemplateType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(name = "template")
class Template(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    var type: DocumentTemplateType? = null,

    var contentType: String? = null,

    @Lob
     var data: ByteArray? = null

) : BaseEntity
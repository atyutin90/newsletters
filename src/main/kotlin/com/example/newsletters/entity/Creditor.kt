package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "creditor")
class Creditor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null,

    @Column(
        name = "name",
        nullable = false,
        length = 256
    )
    var name: String? = null,

    @Column(name = "voice_numbers")
    var voiceNumbers: String? = null,
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Creditor

        if (id != other.id) return false
        if (name != other.name) return false
        if (voiceNumbers != other.voiceNumbers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (voiceNumbers?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Creditor(id=$id, name=$name, voiceNumbers=$voiceNumbers)"
    }


}

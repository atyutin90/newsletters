package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime


@Entity
@Table(name = "request")
class Request(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(
        name = "company_address",
        nullable = false,
        length = 500
    )
    var companyAddress: String? = null,

    @Column(
        name = "company_name",
        nullable = false,
        length = 500
    )
    var companyName: String? = null,

    @Column(
        name = "date_from",
        nullable = true,
        length = 1000
    )
    var dateFrom: ZonedDateTime? = null,

    @Column(
        name = "date_to",
        nullable = true,
        length = 1000
    )
    var dateTo: ZonedDateTime? = null,

    @Column(
        name = "attachment",
        nullable = true,
        length = 1000
    )
    var attachment: String? = null

) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Request

        if (id != other.id) return false
        if (companyAddress != other.companyAddress) return false
        if (companyName != other.companyName) return false
        if (dateFrom != other.dateFrom) return false
        if (dateTo != other.dateTo) return false
        if (attachment != other.attachment) return false

        return true
    }

    override fun toString(): String {
        return "Request(id=$id, companyAddress=$companyAddress, companyName=$companyName, dateFrom=$dateFrom, dateTo=$dateTo, attachment=$attachment)"
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (companyAddress?.hashCode() ?: 0)
        result = 31 * result + (companyName?.hashCode() ?: 0)
        result = 31 * result + (dateFrom?.hashCode() ?: 0)
        result = 31 * result + (dateTo?.hashCode() ?: 0)
        result = 31 * result + (attachment?.hashCode() ?: 0)
        return result
    }
}

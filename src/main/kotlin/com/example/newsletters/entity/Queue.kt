package com.example.newsletters.entity

import com.example.newsletters.entity.enum.QueueType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZonedDateTime

@Entity
@Table(name = "queue")
class Queue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "creditor_id", nullable = true,)
    var creditorId: Long? = null,

    @Column(name = "type", length = 50)
    var type: QueueType? = null,

    @Column(name = "amount")
    var amount: BigDecimal? = null,

    @Column(name = "entry_date")
    var entryDate: LocalDate? = null,

    @Column(name = "obligation_type")
    var obligationType: String? = null,

    @Column(name = "doc_number_reason_claim")
    var documentNumberOfReasonClaim: String? = null,

    @Column(name = "claim_date")
    var claimDate: LocalDate? = null,

    @Column(name = "claim_amount")
    var claimAmount:BigDecimal? = null,

    @Column(name = "determination")
    var determination: String? = null,

    @Column(name = "repayment_date")
    var repaymentDate: LocalDate? = null,

    @Column(name = "repayment_doc_details")
    var repaymentDocumentDetails: String? = null,

    @Column(name = "repayment_amount")
    var repaymentAmount: BigDecimal? = null,

    @Column(name = "outstanding_amount")
    var outstandingAmount: BigDecimal? = null,

    @Column(name = "exclusion_from_register_date")
    var exclusionFromRegisterDate: LocalDate? = null,

    @Column(name = "exclusion_doc")
    var exclusionDocument: String? = null,

    @Column(name = "deposit_location")
    var depositLocation: String? = null,

    @Column(name = "deposit_doc_details")
    var depositDocumentDetails: String? = null,

    @Column(name = "deposit_amount")
    var depositAmount: BigDecimal? = null,

    @Column(name = "fine_type")
    var fineType: String? = null,

    @Column(name = "fine_amount")
    var fineAmount: BigDecimal? = null

): BaseEntity
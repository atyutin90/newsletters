package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(name = "debtor_meeting_question")
class DebtorMeetingQuestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "question", length = 500)
    var question: String? = null,

    @Column(name = "proposed_solution", length = 500)
    var proposedSolution: String? = null,

    @Column(name = "accepted_solution", length = 500)
    var acceptedSolution: String? = null,

    @Column(name = "position")
    var position: Int? = null,

    @Column(name = "debtor_meeting_Id")
    var debtorMeetingId: Long,

) : BaseEntity

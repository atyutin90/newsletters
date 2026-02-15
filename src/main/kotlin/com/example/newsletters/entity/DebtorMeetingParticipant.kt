package com.example.newsletters.entity

import com.example.newsletters.entity.enum.MeetingParticipantType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(name = "debtor_meeting_participant")
class DebtorMeetingParticipant(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "type")
    var type: MeetingParticipantType? = null,

    @Column(name = "debtor_meeting_id")
    var debtorMeetingId: Long,

    @Column(name = "creditor_id", updatable = false, insertable = false)
    var creditorId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "creditor_id")
    val creditor: Creditor? = null,

    @Column(name = "meeting_participant_id", updatable = false, insertable = false)
    var meetingParticipantId: Long? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "meeting_participant_id")
    val meetingParticipant: MeetingParticipant? = null,

    @Column(name = "without_right")
    var withoutRight: Boolean = false

) : BaseEntity

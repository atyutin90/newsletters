package com.example.newsletters.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.ZonedDateTime

@Entity
@Table(name = "meeting_participant")
class MeetingParticipant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null,

    @Column(updatable = false)
    @CreationTimestamp
    override var createdAt: ZonedDateTime? = null,

    @UpdateTimestamp
    override var updatedAt: ZonedDateTime? = null,

    @Column(name = "name", length = 256)
    var name: String? = null,

    @Column(name = "address", length = 500)
    var address: String? = null,

    @OneToMany(mappedBy = "meetingParticipant")
    val debtorMeetingParticipant: MutableSet<DebtorMeetingParticipant> = mutableSetOf()

): BaseEntity

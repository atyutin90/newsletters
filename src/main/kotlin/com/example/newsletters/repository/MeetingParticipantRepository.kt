package com.example.newsletters.repository

import com.example.newsletters.entity.MeetingParticipant
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface MeetingParticipantRepository : JpaRepository<MeetingParticipant, Long>

package com.example.newsletters.service

import com.example.newsletters.dto.model.WorkerMeetingParticipantDto
import com.example.newsletters.entity.WorkerMeetingParticipant
import com.example.newsletters.repository.WorkerMeetingParticipantRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class WorkerMeetingParticipantStorageService(private val repository: WorkerMeetingParticipantRepository) {

    fun getAll(): List<WorkerMeetingParticipantDto> = repository.findAll().map { it.workerMeetingParticipantDTO }

    fun getByWorkerMeetingId(workerMeetingId: Long): List<WorkerMeetingParticipantDto> =
        repository.findByWorkerMeetingId(workerMeetingId).map { it.workerMeetingParticipantDTO }

    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.workerMeetingParticipantDTO }

    fun getById(id: Long) = repository.findById(id).map { it.workerMeetingParticipantDTO }.orElse(null)

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.workerMeetingParticipantDTO }

    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun create(request: WorkerMeetingParticipantDto) = repository.save(request.workerMeetingParticipant)

    @Transactional
    fun update(request: WorkerMeetingParticipantDto) = repository.save(request.workerMeetingParticipant)
}

val WorkerMeetingParticipant.workerMeetingParticipantDTO
    get() = WorkerMeetingParticipantDto(
        id = this.id,
        workerMeetingId = this.workerMeetingId,
        fullName = this.fullName,
        address = this.address,
    )

val WorkerMeetingParticipantDto.workerMeetingParticipant
    get() = WorkerMeetingParticipant(
        id = this.id,
        workerMeetingId = this.workerMeetingId,
        fullName = this.fullName,
        address = this.address,
    )

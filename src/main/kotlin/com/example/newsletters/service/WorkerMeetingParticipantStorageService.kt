package com.example.newsletters.service

import com.example.newsletters.dto.model.WorkerMeetingParticipantDto
import com.example.newsletters.entity.WorkerMeetingParticipant
import com.example.newsletters.repository.WorkerMeetingParticipantRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkerMeetingParticipantStorageService(
    private val repository: WorkerMeetingParticipantRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<WorkerMeetingParticipantDto> =
        repository.findAll()
            .map { it.workerMeetingParticipantDTO }

    fun getByWorkerMeetingId(workerMeetingId: Long): List<WorkerMeetingParticipantDto> =
        repository.findByWorkerMeetingId(workerMeetingId)
            .map { it.workerMeetingParticipantDTO }

    fun getAll(paging: Pageable) =
        repository.findAll(paging)
            .map { it.workerMeetingParticipantDTO }

    fun getById(id: Long): WorkerMeetingParticipantDto =
        repository.findById(id)
            .map { it.workerMeetingParticipantDTO }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) =
        repository.findAllById(ids)
            .map { it.workerMeetingParticipantDTO }

    @Transactional
    fun delete(id: Long) = runCatching {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun create(data: WorkerMeetingParticipantDto) =
        repository.save(data.workerMeetingParticipant).workerMeetingParticipantDTO

    @Transactional
    fun update(data: WorkerMeetingParticipantDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.workerMeetingParticipant).workerMeetingParticipantDTO
    }
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

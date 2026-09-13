package com.example.newsletters.service

import com.example.newsletters.dto.model.WorkerMeetingDto
import com.example.newsletters.entity.WorkerMeeting
import com.example.newsletters.repository.WorkerMeetingRepository
import org.springframework.context.MessageSource
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class WorkerMeetingStorageService(
    private val repository: WorkerMeetingRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<WorkerMeetingDto> =
        repository.findAll()
            .map { it.workerMeetingDTO }

    fun getByDebtorId(debtorId: Long): List<WorkerMeetingDto> =
        repository.findByDebtorId(debtorId)
            .map { it.workerMeetingDTO }

    fun getAll(paging: Pageable) =
        repository.findAll(paging)
            .map { it.workerMeetingDTO }

    fun getById(id: Long): WorkerMeetingDto =
        repository.findById(id)
            .map { it.workerMeetingDTO }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) =
        repository.findAllById(ids)
            .map { it.workerMeetingDTO }

    @Transactional
    fun delete(id: Long) = run {
        repository.deleteById(id)
    }

    @Transactional
    fun create(data: WorkerMeetingDto) =
        repository.save(data.workerMeeting).workerMeetingDTO

    @Transactional
    fun update(data: WorkerMeetingDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.workerMeeting).workerMeetingDTO
    }
}

val WorkerMeeting.workerMeetingDTO
    get() = WorkerMeetingDto(
        id = this.id,
        debtorId = this.debtorId,
        date = this.date,
        time = this.time,
        registrationTimeFrom = this.registrationTimeFrom,
        registrationTimeTo = this.registrationTimeTo,
        address = this.address,
        topic = this.topic,
    )

val WorkerMeetingDto.workerMeeting
    get() = WorkerMeeting(
        id = this.id,
        debtorId = this.debtorId,
        date = this.date,
        time = this.time,
        registrationTimeFrom = this.registrationTimeFrom,
        registrationTimeTo = this.registrationTimeTo,
        address = this.address,
        topic = this.topic,
    )

package com.example.newsletters.service

import com.example.newsletters.dto.DebtorMeetingDto
import com.example.newsletters.dto.RequestDto
import com.example.newsletters.dto.WorkerMeetingDto
import com.example.newsletters.entity.DebtorMeeting
import com.example.newsletters.entity.Request
import com.example.newsletters.entity.RequestDestination
import com.example.newsletters.entity.WorkerMeeting
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.RequestRepository
import com.example.newsletters.repository.WorkerMeetingRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.time.ZonedDateTime

@Service
class WorkerMeetingStorageService(private val repository: WorkerMeetingRepository) {

    fun getAll(): List<WorkerMeetingDto> = repository.findAll().map { it.workerMeetingDTO }

    fun getByDebtorId(debtorId: Long): List<WorkerMeetingDto> = repository.findByDebtorId(debtorId).map { it.workerMeetingDTO }

    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.workerMeetingDTO }

    fun getById(id: Long) = repository.findById(id).map { it.workerMeetingDTO }.orElse(null)

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.workerMeetingDTO }

    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun create(request: WorkerMeetingDto) = repository.save(request.workerMeeting)

    @Transactional
    fun update(request: WorkerMeetingDto) = repository.save(request.workerMeeting)
}

val WorkerMeeting.workerMeetingDTO get() = WorkerMeetingDto(
    id = this.id,
    debtorId = this.debtorId,
    date = this.date,
    time = this.time,
    registrationTimeFrom = this.registrationTimeFrom,
    registrationTimeTo = this.registrationTimeTo,
    address = this.address,
)

val WorkerMeetingDto.workerMeeting get() = WorkerMeeting(
    id = this.id,
    debtorId = this.debtorId,
    date = this.date,
    time = this.time,
    registrationTimeFrom = this.registrationTimeFrom,
    registrationTimeTo = this.registrationTimeTo,
    address = this.address,
)

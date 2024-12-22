package com.example.newsletters.service

import com.example.newsletters.dto.DebtorMeetingDto
import com.example.newsletters.dto.RequestDto
import com.example.newsletters.entity.DebtorMeeting
import com.example.newsletters.entity.Request
import com.example.newsletters.entity.RequestDestination
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.RequestRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.time.ZonedDateTime

@Service
class DebtorMeetingStorageService(private val repository: DebtorMeetingRepository) {

    fun getAll(): List<DebtorMeetingDto> = repository.findAll().map { it.debtorMeetingDTO }

    fun getByDebtorId(debtorId: Long): List<DebtorMeetingDto> = repository.findByDebtorId(debtorId).map { it.debtorMeetingDTO }

    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.debtorMeetingDTO }

    fun getById(id: Long) = repository.findById(id).map { it.debtorMeetingDTO }.orElse(null)

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.debtorMeetingDTO }

    fun delete(id: Long) = repository.deleteById(id)

    fun create(request: DebtorMeetingDto) = repository.save(request.debtorMeeting)

    fun update(request: DebtorMeetingDto) = repository.save(request.debtorMeeting)
}

val DebtorMeeting.debtorMeetingDTO get() = DebtorMeetingDto(
    id = this.id,
    debtorId = this.debtorId,
    date = this.date,
    time = this.time,
    registrationTimeFrom = this.registrationTimeFrom,
    registrationTimeTo = this.registrationTimeTo,
    address = this.address,
    familiarizationDateFrom = this.familiarizationDateFrom,
    familiarizationDateTo = this.familiarizationDateTo
)

val DebtorMeetingDto.debtorMeeting get() = DebtorMeeting(
    id = this.id,
    debtorId = this.debtorId,
    date = this.date,
    time = this.time,
    registrationTimeFrom = this.registrationTimeFrom,
    registrationTimeTo = this.registrationTimeTo,
    address = this.address,
    familiarizationDateFrom = this.familiarizationDateFrom,
    familiarizationDateTo = this.familiarizationDateTo
)

package com.example.newsletters.service

import com.example.newsletters.dto.model.DebtorMeetingDto
import com.example.newsletters.entity.DebtorMeeting
import com.example.newsletters.entity.DebtorMeetingQuestion
import com.example.newsletters.repository.DebtorMeetingQuestionRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.QuestionRepository
import com.example.newsletters.service.report.DocumentGeneratorService
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DebtorMeetingStorageService(
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val questionRepository: QuestionRepository,
    private val debtorMeetingQuestionRepository: DebtorMeetingQuestionRepository,
    private val documentGeneratorService: DocumentGeneratorService
) {

    fun getAll(): List<DebtorMeetingDto> = debtorMeetingRepository.findAll().map { it.debtorMeetingDTO }

    fun getByDebtorId(debtorId: Long): List<DebtorMeetingDto> =
        debtorMeetingRepository.findByDebtorId(debtorId).map { it.debtorMeetingDTO }

    fun getAll(paging: Pageable) = debtorMeetingRepository.findAll(paging).map { it.debtorMeetingDTO }

    fun getById(id: Long) = debtorMeetingRepository.findById(id).map { it.debtorMeetingDTO }.orElse(null)

    fun getByIds(ids: List<Long>) = debtorMeetingRepository.findAllById(ids).map { it.debtorMeetingDTO }

    fun delete(id: Long) = debtorMeetingRepository.deleteById(id)

    @Transactional
    fun create(request: DebtorMeetingDto) {
        val questions = questionRepository.findAll()
        val debtorMeeting = debtorMeetingRepository.save(request.debtorMeeting)
        questions
            .map {
                DebtorMeetingQuestion(
                    question = it.value,
                    position = it.position,
                    debtorMeetingId = debtorMeeting.id!!
                )
            }.forEach { debtorMeetingQuestionRepository.save(it) }
        //Процесс генерации документов
        documentGeneratorService.generate(request.debtorId)
    }

    @Transactional
    fun update(request: DebtorMeetingDto) {
        debtorMeetingRepository.save(request.debtorMeeting)
        //Процесс генерации документов
        documentGeneratorService.generate(request.debtorId)
    }
}

val DebtorMeeting.debtorMeetingDTO
    get() = DebtorMeetingDto(
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

val DebtorMeetingDto.debtorMeeting
    get() = DebtorMeeting(
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

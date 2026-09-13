package com.example.newsletters.service

import com.example.newsletters.dto.model.DebtorMeetingQuestionDto
import com.example.newsletters.entity.DebtorMeetingQuestion
import com.example.newsletters.repository.DebtorMeetingQuestionRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DebtorMeetingQuestionStorageService(
    private val repository: DebtorMeetingQuestionRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<DebtorMeetingQuestionDto> =
        repository.findAll()
            .map { it.debtorMeetingQuestionDTO }

    fun getByWorkerMeetingId(debtorMeetingId: Long) =
        repository.findByDebtorMeetingIdOrderByPositionAsc(debtorMeetingId)
            .map { it.debtorMeetingQuestionDTO }

    fun getAll(paging: Pageable) =
        repository.findAll(paging)
            .map { it.debtorMeetingQuestionDTO }

    fun getById(id: Long): DebtorMeetingQuestionDto =
        repository.findById(id)
            .map { it.debtorMeetingQuestionDTO }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.debtorMeetingQuestionDTO }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun create(data: DebtorMeetingQuestionDto) =
        repository.save(data.debtorMeetingQuestion).debtorMeetingQuestionDTO

    @Transactional
    fun update(data: DebtorMeetingQuestionDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.debtorMeetingQuestion).debtorMeetingQuestionDTO
    }

    val DebtorMeetingQuestion.debtorMeetingQuestionDTO
        get() = DebtorMeetingQuestionDto(
            id = this.id,
            question = this.question,
            proposedSolution = this.proposedSolution,
            acceptedSolution = this.acceptedSolution,
            position = this.position,
            debtorMeetingId = this.debtorMeetingId,
        )

    val DebtorMeetingQuestionDto.debtorMeetingQuestion
        get() = DebtorMeetingQuestion(
            id = this.id,
            question = this.question,
            proposedSolution = this.proposedSolution,
            acceptedSolution = this.acceptedSolution,
            position = this.position,
            debtorMeetingId = this.debtorMeetingId,
        )
}

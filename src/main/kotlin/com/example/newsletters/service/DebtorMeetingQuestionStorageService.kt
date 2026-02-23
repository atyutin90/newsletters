package com.example.newsletters.service

import com.example.newsletters.dto.model.DebtorMeetingQuestionDto
import com.example.newsletters.entity.DebtorMeetingQuestion
import com.example.newsletters.repository.DebtorMeetingQuestionRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DebtorMeetingQuestionStorageService(private val repository: DebtorMeetingQuestionRepository) {

    fun getAll(): List<DebtorMeetingQuestionDto> = repository.findAll().map { it.debtorMeetingQuestionDTO }

    fun getByWorkerMeetingId(debtorMeetingId: Long) =
        repository.findByDebtorMeetingIdOrderByPositionAsc(debtorMeetingId).map { it.debtorMeetingQuestionDTO }

    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.debtorMeetingQuestionDTO }

    fun getById(id: Long) = repository.findById(id).map { it.debtorMeetingQuestionDTO }.orElse(null)

    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.debtorMeetingQuestionDTO }

    fun delete(id: Long) = repository.deleteById(id)

    @Transactional
    fun create(request: DebtorMeetingQuestionDto) = repository.save(request.debtorMeetingQuestion)

    @Transactional
    fun update(request: DebtorMeetingQuestionDto) = repository.save(request.debtorMeetingQuestion)

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

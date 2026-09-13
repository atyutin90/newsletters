package com.example.newsletters.service

import com.example.newsletters.dto.model.QueueDto
import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.Queue
import com.example.newsletters.entity.enum.QueueType.THIRD_DEPOSIT
import com.example.newsletters.entity.enum.QueueType.Companion.queueTypeOf
import com.example.newsletters.repository.QueueRepository
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class QueueStorageService(
    private val repository: QueueRepository,
    messageSource: MessageSource
) : AbstractRepositoryService(messageSource) {

    fun getAll(): List<QueueDto> =
        repository.findAll()
            .map { it.queueDto }

    fun getAll(paging: Pageable) =
        repository.findAll(paging)
            .map { it.queueDto }

    fun getByCreditorId(creditorId: Long): List<QueueDto> =
        repository.findByCreditorId(creditorId)
            .map { it.queueDto }

    fun getById(id: Long): QueueDto =
        repository.findById(id).map { it.queueDto }
            .orElseThrow { notExist(id) }

    fun getByIds(ids: List<Long>) =
        repository.findAllById(ids)
            .map { it.queueDto }

    @Transactional
    fun delete(id: Long) = run {
        val result = repository.existsById(id)
        if (!result) notExist(id)
        repository.deleteById(id)
    }

    @Transactional
    fun create(data: QueueDto) = repository.save(data.queue).queueDto

    @Transactional
    fun update(data: QueueDto) = run {
        val result = data.id?.let { repository.existsById(data.id) } ?: false
        if (!result) notExist(data.id)
        repository.save(data.queue).queueDto
    }
}

val Queue.queueDto
    get() = QueueDto(
        id = id,
        creditorId = creditorId,
        type = type?.name,
        entryDate = entryDate,
        obligationType = obligationType,
        documentNumberOfReasonClaim = documentNumberOfReasonClaim,
        claimDate = claimDate,
        claimAmount = claimAmount,
        determination = determination,
        repaymentDate = repaymentDate,
        repaymentDocumentDetails = repaymentDocumentDetails,
        repaymentAmount = repaymentAmount,
        outstandingAmount = outstandingAmount,
        exclusionFromRegisterDate = exclusionFromRegisterDate,
        exclusionFromRegister = exclusionFromRegister,
        exclusionDocument = exclusionDocument,
        depositLocation = depositLocation,
        depositDocumentDetails = depositDocumentDetails,
        depositAmount = depositAmount,
        principalAmount = principalAmount,
        stateDutyAmount = stateDutyAmount,
        percentAmount = percentAmount,
        penaltyAmount = penaltyAmount,
        fine = fine,
        percentOnPercentAmount = percentOnPercentAmount
    )

val QueueDto.queue
    get() = run {
        val queueType = queueTypeOf(type)
        val exclusionFromRegister = exclusionFromRegister ?: false
        Queue(
            id = id,
            creditor = Creditor(id = creditorId),
            type = queueType,
            entryDate = entryDate,
            obligationType = obligationType,
            documentNumberOfReasonClaim = documentNumberOfReasonClaim,
            claimDate = claimDate,
            claimAmount = claimAmount,
            determination = determination,
            repaymentDate = if (exclusionFromRegister) repaymentDate else null,
            repaymentDocumentDetails = if (exclusionFromRegister) repaymentDocumentDetails else null,
            repaymentAmount = if (exclusionFromRegister) repaymentAmount else null,
            outstandingAmount = if (exclusionFromRegister) outstandingAmount else null,
            exclusionFromRegisterDate = if (exclusionFromRegister) exclusionFromRegisterDate else null,
            exclusionDocument = if (exclusionFromRegister) exclusionDocument else null,
            exclusionFromRegister = exclusionFromRegister,
            depositLocation = if (queueType == THIRD_DEPOSIT) depositLocation else null,
            depositDocumentDetails = if (queueType == THIRD_DEPOSIT) depositDocumentDetails else null,
            depositAmount = if (queueType == THIRD_DEPOSIT) depositAmount else null,
            principalAmount = principalAmount,
            stateDutyAmount = stateDutyAmount,
            percentAmount = percentAmount,
            penaltyAmount = penaltyAmount,
            fine = fine,
            percentOnPercentAmount = percentOnPercentAmount
        )
    }

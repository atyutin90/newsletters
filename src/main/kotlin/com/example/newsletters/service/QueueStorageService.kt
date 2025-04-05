package com.example.newsletters.service

import com.example.newsletters.dto.QueueDto
import com.example.newsletters.entity.Queue
import com.example.newsletters.entity.enum.QueueType
import com.example.newsletters.repository.QueueRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class QueueStorageService(private val repository: QueueRepository) {
    fun getAll(): List<QueueDto> = repository.findAll().map { it.queueDto }
    fun getAll(paging: Pageable) = repository.findAll(paging).map { it.queueDto }
    fun getByCreditorId(creditorId: Long): List<QueueDto> = repository.findByCreditorId(creditorId).map { it.queueDto }
    fun getById(id: Long) = repository.findById(id).map { it.queueDto }.orElse(null)
    fun getByIds(ids: List<Long>) = repository.findAllById(ids).map { it.queueDto }
    fun delete(id: Long) = repository.deleteById(id)
    @Transactional
    fun create(creditor: QueueDto) = repository.save(creditor.queue)
    @Transactional
    fun update(creditor: QueueDto) = repository.save(creditor.queue)
}

val Queue.queueDto get() = QueueDto(
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

val QueueDto.queue get() = Queue(
    id = id,
    creditorId = creditorId,
    type = QueueType.queueTypeOf(type),
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
    exclusionFromRegister = exclusionFromRegister ?: false,
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

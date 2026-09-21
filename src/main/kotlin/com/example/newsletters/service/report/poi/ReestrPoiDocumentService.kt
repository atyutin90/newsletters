package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.dto.report.poi.PoiTableRow
import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.Document
import com.example.newsletters.entity.Queue
import com.example.newsletters.entity.enum.QueueType.FIRST
import com.example.newsletters.entity.enum.QueueType.SECOND
import com.example.newsletters.entity.enum.QueueType.THIRD_DEPOSIT
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.QueueRepository
import org.springframework.stereotype.Service

private const val REGISTRY_DATE = "registryDate"

private const val FIRST_QUEUE_INFO_ROW = "firstQueueInfoRow"
private const val FIRST_QUEUE_EXIGENCE_ROW = "firstQueueExigenceRow"
private const val FIRST_QUEUE_REPAYMENT_ROW = "firstQueueRepaymentRow"
private const val SECOND_QUEUE_INFO_ROW = "secondQueueInfoRow"
private const val SECOND_QUEUE_EXIGENCE_ROW = "secondQueueExigenceRow"
private const val SECOND_QUEUE_REPAYMENT_ROW = "secondQueueRepaymentRow"
private const val THIRD_QUEUE_INFO_ROW = "thirdQueueInfoRow"
private const val THIRD_QUEUE_EXIGENCE_ROW = "thirdQueueExigenceRow"
private const val THIRD_QUEUE_OBLIGATION_ROW = "thirdQueueObligationRow"
private const val THIRD_QUEUE_REPAYMENT_ROW = "thirdQueueRepaymentRow"

private const val ENTRY_DATE = "entryDate"
private const val CREDITOR_PASSPORT = "creditorPassport"
private const val DEPOSIT_LOCATION = "depositLocation"
private const val OBLIGATION_TYPE = "obligationType"
private const val EXECUTION_WRIT = "executionWrit"
private const val EXECUTION_DATE = "executionDate"
private const val PRINCIPAL_AMOUNT = "principalAmount"
private const val DETERMINATION = "determination"
private const val REPAYMENT_DATE = "repaymentDate"
private const val REPAYMENT_DOCUMENT = "repaymentDocument"
private const val REPAYMENT_AMOUNT = "repaymentAmount"
private const val OUTSTANDING_AMOUNT = "outstandingAmount"
private const val EXCLUSION_DATE = "exclusionDate"
private const val EXCLUSION_DOCUMENT = "exclusionDocument"
private const val DEPOSIT_DOCUMENT = "depositDocument"
private const val DEPOSIT_AMOUNT = "depositAmount"

@Service
class ReestrPoiDocumentService(
    private val debtorRepository: DebtorRepository,
    private val creditorRepository: CreditorRepository,
    private val queueRepository: QueueRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val poiTemplateService: PoiTemplateService,
) : PoiService {

    override fun generate(document: Document): ByteArray {
        val debtor = debtorRepository.findById(document.debtorId).orElseThrow { IllegalStateException("Не найден должник ${document.debtorId}") }
        val manager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val queues = creditorRepository.findByDebtorId(document.debtorId)
            .flatMap { creditor ->
                queueRepository.findByCreditorId(creditor.id!!).onEach { queue ->
                    if (queue.creditor == null) queue.creditor = creditor
                }
            }

        val firstQueues = queues.filter { it.type == FIRST }
        val secondQueues = queues.filter { it.type == SECOND }
        val thirdQueues = queues.filter { it.type == THIRD_DEPOSIT }

        return poiTemplateService.generate(
            configuredPath = document.templateResourcePath.orEmpty(),
            data = PoiReportData(
                values = mapOf(
                    value(DEBTOR_NAME) to debtor.name.orEmpty(),
                    value(REGISTRY_DATE) to debtor.registryDate?.format(shortDatePattern()).orEmpty(),
                    value(MANAGER_NAME) to manager?.fullName.orEmpty(),
                ),
                tableRows = listOf(
                    infoRows(FIRST_QUEUE_INFO_ROW, firstQueues),
                    exigenceRows(FIRST_QUEUE_EXIGENCE_ROW, firstQueues),
                    repaymentRows(FIRST_QUEUE_REPAYMENT_ROW, firstQueues),
                    infoRows(SECOND_QUEUE_INFO_ROW, secondQueues),
                    exigenceRows(SECOND_QUEUE_EXIGENCE_ROW, secondQueues),
                    repaymentRows(SECOND_QUEUE_REPAYMENT_ROW, secondQueues),
                    thirdQueueInfoRows(thirdQueues),
                    exigenceRows(THIRD_QUEUE_EXIGENCE_ROW, thirdQueues),
                    thirdQueueObligationRows(thirdQueues),
                    repaymentRows(THIRD_QUEUE_REPAYMENT_ROW, thirdQueues),
                ),
            ),
        )
    }

    private fun infoRows(markerName: String, queues: List<Queue>) = tableRows(markerName, queues) { marker, queue, rowNumber ->
        val creditor = queue.creditor
        mapOf(
            marker to rowNumber,
            value(ENTRY_DATE) to queue.entryDate.format(),
            value(CREDITOR_NAME) to creditor?.name.orEmpty(),
            value(CREDITOR_PASSPORT) to creditor.passport(),
            value(CREDITOR_ADDRESS) to creditor?.address.orEmpty(),
        )
    }

    private fun thirdQueueInfoRows(queues: List<Queue>) = tableRows(THIRD_QUEUE_INFO_ROW, queues) { marker, queue, rowNumber ->
        val creditor = queue.creditor
        mapOf(
            marker to rowNumber,
            value(ENTRY_DATE) to queue.entryDate.format(),
            value(CREDITOR_NAME) to creditor?.name.orEmpty(),
            value(CREDITOR_PASSPORT) to creditor.passport(),
            value(DEPOSIT_LOCATION) to queue.depositLocation.orEmpty(),
            value(CREDITOR_ADDRESS) to creditor?.address.orEmpty(),
        )
    }

    private fun exigenceRows(markerName: String, queues: List<Queue>) = tableRows(markerName, queues) { marker, queue, rowNumber ->
        mapOf(
            marker to rowNumber,
            value(ENTRY_DATE) to queue.entryDate.format(),
            value(OBLIGATION_TYPE) to queue.obligationType.orEmpty(),
            value(EXECUTION_WRIT) to queue.creditor?.executionWrit.orEmpty(),
            value(EXECUTION_DATE) to queue.creditor?.executionDate.format(),
            value(PRINCIPAL_AMOUNT) to queue.principalAmount.format(),
            value(DETERMINATION) to queue.determination.orEmpty(),
        )
    }

    private fun repaymentRows(markerName: String, queues: List<Queue>) = tableRows(markerName, queues) { marker, queue, rowNumber ->
        mapOf(
            marker to rowNumber,
            value(REPAYMENT_DATE) to queue.repaymentDate.format(),
            value(REPAYMENT_DOCUMENT) to queue.repaymentDocumentDetails.orEmpty(),
            value(REPAYMENT_AMOUNT) to queue.repaymentAmount.format(),
            value(OUTSTANDING_AMOUNT) to queue.outstandingAmount.format(),
            value(EXCLUSION_DATE) to queue.exclusionFromRegisterDate.format(),
            value(EXCLUSION_DOCUMENT) to queue.exclusionDocument.orEmpty(),
        )
    }

    private fun thirdQueueObligationRows(queues: List<Queue>) = tableRows(THIRD_QUEUE_OBLIGATION_ROW, queues) { marker, queue, rowNumber ->
        mapOf(
            marker to rowNumber,
            value(ENTRY_DATE) to queue.entryDate.format(),
            value(DEPOSIT_DOCUMENT) to queue.depositDocumentDetails.orEmpty(),
            value(DEPOSIT_AMOUNT) to queue.depositAmount.format(),
        )
    }

    private fun tableRows(
        markerName: String,
        queues: List<Queue>,
        values: (marker: String, queue: Queue, rowNumber: String) -> Map<String, String>,
    ): PoiTableRow {
        val marker = value(markerName)
        return PoiTableRow(
            marker = marker,
            values = queues.mapIndexed { index, queue -> values(marker, queue, (index + 1).toString()) },
        )
    }

    private fun java.time.LocalDate?.format(): String = this?.format(shortDatePattern()).orEmpty()

    private fun java.math.BigDecimal?.format(): String = this?.toPlainString().orEmpty()

    private fun Creditor?.passport(): String = listOfNotNull(this?.passportSerial, this?.passportNumber)
        .joinToString(" ")
}

package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.helper.QueueHelper.Companion.getThirdQueueAmountByCreditor
import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.dto.report.poi.PoiTableRow
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.QueueRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

private const val MEETING_ADDRESS_AND_DATE = "meetingAddressAndDate"
private const val REGISTRATION_TIME_FROM = "registrationTimeFrom"
private const val REGISTRATION_TIME_TO = "registrationTimeTo"
private const val TOTAL_QUEUE_AMOUNT = "totalQueueAmount"
private const val TOTAL_ROUND_QUEUE_AMOUNT = "totalRoundQueueAmount"

@Service
class MeetingParticipantsRegistrationJournalPoiService(
    private val debtorRepository: DebtorRepository,
    private val creditorRepository: CreditorRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val queueRepository: QueueRepository,
    private val poiTemplateService: PoiTemplateService,
) : PoiService {

    override fun generate(document: Document): ByteArray {
        val debtor = debtorRepository.findById(document.debtorId).orElseThrow { IllegalStateException("Не найден должник ${document.debtorId}") }
        val meeting = debtorMeetingRepository.findByDebtorId(document.debtorId).firstOrNull() ?: throw IllegalStateException("Не найдено собрание кредиторов должника ${document.debtorId}")
        val manager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) } ?: throw IllegalStateException("Не найден временный управляющий должника ${document.debtorId}")
        val creditors = creditorRepository.findByDebtorId(document.debtorId)
        val queues = queueRepository.findByCreditorIds(creditors.mapNotNull { it.id })
        val queueAmounts = creditors.associateWith { creditor -> getThirdQueueAmountByCreditor(queues, creditor.id, false) }
        val roundedQueueAmounts = creditors.associateWith { creditor -> getThirdQueueAmountByCreditor(queues, creditor.id, true) }

        val values = mapOf(
            value(DEBTOR_NAME) to debtor.name.orEmpty(),
            value(MEETING_ADDRESS_AND_DATE) to listOf(
                meeting.address.orEmpty(),
                meeting.date?.format(longDatePattern()).orEmpty(),
            ).filter { it.isNotBlank() }.joinToString(" "),
            value(REGISTRATION_TIME_FROM) to meeting.registrationTimeFrom?.toString().orEmpty(),
            value(REGISTRATION_TIME_TO) to meeting.registrationTimeTo?.toString().orEmpty(),
            value(TOTAL_QUEUE_AMOUNT) to queueAmounts.values.fold(ZERO, BigDecimal::add).toPlainString(),
            value(TOTAL_ROUND_QUEUE_AMOUNT) to roundedQueueAmounts.values.fold(ZERO, BigDecimal::add).toPlainString(),
            value(MANAGER_NAME) to manager.fullName.orEmpty(),
        )
        val creditorRows = creditors.mapIndexed { index, creditor ->
            mapOf(
                value(ROW_NUMBER) to (index + 1).toString(),
                value(CREDITOR_NAME) to creditor.name.orEmpty(),
                value(CREDITOR_ADDRESS) to creditor.address.orEmpty(),
                value(QUEUE_AMOUNT) to queueAmounts.getValue(creditor).toPlainString(),
                value(ROUND_QUEUE_AMOUNT) to roundedQueueAmounts.getValue(creditor).toPlainString(),
            )
        }

        return poiTemplateService.generate(
            configuredPath = document.templateResourcePath!!,
            data = PoiReportData(values, listOf(PoiTableRow(value(CREDITOR_NAME), creditorRows))),
        )
    }
}

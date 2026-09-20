package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.helper.QueueHelper.Companion.getThirdQueueAmountByCreditor
import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.QueueRepository
import org.springframework.stereotype.Service

@Service
class BulletinPoiDocumentService(
    private val debtorRepository: DebtorRepository,
    private val creditorRepository: CreditorRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val queueRepository: QueueRepository,
    private val poiTemplateService: PoiTemplateService,
) : PoiService {
    override fun generate(document: Document): ByteArray = run {

        val debtor = debtorRepository.findById(document.debtorId).orElseThrow { IllegalStateException("Не найден должник ${document.debtorId}") }
        val creditorId = document.info?.creditorId ?: throw IllegalStateException("Для бюллетеня не указан кредитор")
        val creditor = creditorRepository.findById(creditorId).orElseThrow { IllegalStateException("Не найден кредитор $creditorId") }
        val meeting = debtorMeetingRepository.findByDebtorId(document.debtorId).firstOrNull()
        val manager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val queues = document.info?.creditorId?.let { queueRepository.findByCreditorId(it) } ?: emptyList()
        val voteAmount = getThirdQueueAmountByCreditor(queues, creditorId)

        val values = mapOf(
            value(DEBTOR_NAME) to debtor.name.orEmpty(),
            value(MEETING_ADDRESS) to meeting?.address.orEmpty(),
            value(MEETING_DATE) to meeting?.date?.format(longDatePattern()).orEmpty(),
            value(MANAGER_NAME) to manager?.fullName.orEmpty(),
            value(CREDITOR_NAME) to creditor.name.orEmpty(),
            value(VOTE_AMOUNT) to voteAmount.toPlainString(),
            value(SRO_DATA) to manager?.sroData.orEmpty()
        )

        poiTemplateService.generate(
            configuredPath = document.templateResourcePath.orEmpty(),
            data = PoiReportData(values),
        )
    }
}

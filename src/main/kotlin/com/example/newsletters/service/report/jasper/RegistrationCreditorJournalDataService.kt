package com.example.newsletters.service.report.jasper

import com.example.newsletters.converter.DomainToReport.toReportArbitrationManager
import com.example.newsletters.converter.DomainToReport.toReportCreditor
import com.example.newsletters.converter.DomainToReport.toReportDebtor
import com.example.newsletters.converter.DomainToReport.toReportDebtorMeeting
import com.example.newsletters.converter.DomainToReport.toReportQueue
import com.example.newsletters.dto.report.helper.QueueHelper
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.QueueRepository
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Service

@Service
class RegistrationCreditorJournalDataService(
    private val debtorRepository: DebtorRepository,
    private val creditorRepository: CreditorRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val queueRepository: QueueRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository
    ): JasperDataService {

    override fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> = run {
        val debtorId = document.debtorId
        val creditors = creditorRepository.findByDebtorId(debtorId)
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val debtorMeeting = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()
        val arbitrationManager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val queues = queueRepository.findByCreditorIds(creditors.mapNotNull { it.id })
        val source = JRBeanCollectionDataSource(creditors.map { it.toReportCreditor() })
        val params = hashMapOf(
            DEBTOR to debtor?.toReportDebtor(),
            DEBTOR_MEETING to debtorMeeting?.toReportDebtorMeeting(),
            ARBITRATION_MANAGER to arbitrationManager?.toReportArbitrationManager(),
            QUEUES to queues.map { it.toReportQueue() },
            QUEUE_HELPER to QueueHelper()
        )
        source to params
    }
}

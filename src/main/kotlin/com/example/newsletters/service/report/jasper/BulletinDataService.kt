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
import com.example.newsletters.dto.report.Creditor as ReportCreditor

@Service
class BulletinDataService(
    private val debtorRepository: DebtorRepository,
    private val creditorRepository: CreditorRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val queueRepository: QueueRepository
): JasperDataService {

    override fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> = run {
        val debtorId = document.debtorId
        val creditor = document.info?.creditorId?.let { id -> creditorRepository.findById(id).orElse(null) }
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val debtorMeeting = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()
        val arbitrationManager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val queues = creditor?.id?.let { queueRepository.findByCreditorId(it) } ?: listOf()
        val dataSource = JRBeanCollectionDataSource(creditor?.toReportCreditor()?.let { listOf(it) } ?: listOf<ReportCreditor>())
        val parameters = hashMapOf(
            DEBTOR to debtor?.toReportDebtor(),
            DEBTOR_MEETING to debtorMeeting?.toReportDebtorMeeting(),
            ARBITRATION_MANAGER to arbitrationManager?.toReportArbitrationManager(),
            QUEUES to queues.map { it.toReportQueue() },
            QUEUE_HELPER to QueueHelper()
        )
        dataSource to parameters
    }
}

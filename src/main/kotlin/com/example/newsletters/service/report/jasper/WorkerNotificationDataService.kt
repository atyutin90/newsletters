package com.example.newsletters.service.report.jasper

import com.example.newsletters.converter.DomainToReport.toReportArbitrationManager
import com.example.newsletters.converter.DomainToReport.toReportDebtor
import com.example.newsletters.converter.DomainToReport.toReportWorkerMeeting
import com.example.newsletters.entity.Document
import com.example.newsletters.entity.WorkerMeeting
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.WorkerMeetingRepository
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Service

@Service
class WorkerNotificationDataService(
    private val debtorRepository: DebtorRepository,
    private val workerMeetingRepository: WorkerMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository
    ) : JasperDataService {

    override fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>>  = run {
        val debtorId = document.debtorId
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val workerMeeting = workerMeetingRepository.findByDebtorId(debtorId).firstOrNull()
        val arbitrationManager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val reportWorkerMeeting = workerMeeting?.toReportWorkerMeeting()
        val source = JRBeanCollectionDataSource(reportWorkerMeeting?.let { listOf(it) } ?: listOf<WorkerMeeting>())
        val params = hashMapOf(
            DEBTOR to debtor?.toReportDebtor(),
            WORKER_MEETING to reportWorkerMeeting,
            ARBITRATION_MANAGER to arbitrationManager?.toReportArbitrationManager(),
        )
        source to params
    }
}

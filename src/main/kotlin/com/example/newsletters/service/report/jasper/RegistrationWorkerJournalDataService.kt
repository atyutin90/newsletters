package com.example.newsletters.service.report.jasper

import com.example.newsletters.converter.DomainToReport.toReportArbitrationManager
import com.example.newsletters.converter.DomainToReport.toReportDebtor
import com.example.newsletters.converter.DomainToReport.toReportWorkerMeeting
import com.example.newsletters.converter.DomainToReport.toReportWorkerMeetingParticipant
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.WorkerMeetingParticipantRepository
import com.example.newsletters.repository.WorkerMeetingRepository
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Service

@Service
class RegistrationWorkerJournalDataService(
    private val debtorRepository: DebtorRepository,
    private val workerMeetingRepository: WorkerMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val workerMeetingParticipantRepository: WorkerMeetingParticipantRepository,
): JasperDataService {
    override fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> = run {
        val debtorId = document.debtorId
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val workerMeeting = workerMeetingRepository.findByDebtorId(debtorId).firstOrNull()
        val participants = workerMeeting?.id?.let { workerMeetingParticipantRepository.findByWorkerMeetingId(it) } ?: listOf()
        val arbitrationManager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val source = JRBeanCollectionDataSource(participants.map { it.toReportWorkerMeetingParticipant() })
        val params = hashMapOf(
            DEBTOR to debtor?.toReportDebtor(),
            WORKER_MEETING to workerMeeting?.toReportWorkerMeeting(),
            ARBITRATION_MANAGER to arbitrationManager?.toReportArbitrationManager(),
        )
        source to params
    }
}

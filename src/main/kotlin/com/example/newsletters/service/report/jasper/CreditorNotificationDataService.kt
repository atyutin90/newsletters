package com.example.newsletters.service.report.jasper

import com.example.newsletters.converter.DomainToReport.toReportArbitrationManager
import com.example.newsletters.converter.DomainToReport.toReportDebtor
import com.example.newsletters.converter.DomainToReport.toReportDebtorMeeting
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DebtorRepository
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Service
import com.example.newsletters.dto.report.Debtor as ReportDebtor

@Service
class CreditorNotificationDataService(
    private val debtorRepository: DebtorRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository
) : JasperDataService {
    override fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> = run {
        val debtorId = document.debtorId
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val debtorMeeting = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()
        val arbitrationManager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val reportDebtor = debtor?.toReportDebtor()
        val source = JRBeanCollectionDataSource(reportDebtor?.let { listOf(it) } ?: listOf<ReportDebtor>())
        val params = hashMapOf(
            DEBTOR to reportDebtor,
            DEBTOR_MEETING to debtorMeeting?.toReportDebtorMeeting(),
            ARBITRATION_MANAGER to arbitrationManager?.toReportArbitrationManager(),
        )
        source to params
    }
}

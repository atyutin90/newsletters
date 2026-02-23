package com.example.newsletters.service.report.jasper

import com.example.newsletters.converter.DomainToReport.toReportArbitrationManager
import com.example.newsletters.converter.DomainToReport.toReportDebtor
import com.example.newsletters.converter.DomainToReport.toReportRequest
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.RequestRepository
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Service
import com.example.newsletters.dto.report.Debtor as ReportDebtor

@Service
class RequestDataService(
    private val debtorRepository: DebtorRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val requestRepository: RequestRepository
) : JasperDataService {
    override fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> = run {
        val debtorId = document.debtorId
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val arbitrationManager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val request = document.info?.requestId?.let { id -> requestRepository.findById(id).orElse(null) }
        val reportDebtor = debtor?.toReportDebtor()
        val source = JRBeanCollectionDataSource(reportDebtor?.let { listOf(it) } ?: listOf<ReportDebtor>() )
        val params = hashMapOf(
            REQUEST_NUMBER to document.id,
            DEBTOR to reportDebtor,
            REQUEST_PARAM to request?.toReportRequest(),
            ARBITRATION_MANAGER to arbitrationManager?.toReportArbitrationManager(),
        )
        source to params
    }
}

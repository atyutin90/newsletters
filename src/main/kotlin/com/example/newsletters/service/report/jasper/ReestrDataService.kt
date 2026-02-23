package com.example.newsletters.service.report.jasper

import com.example.newsletters.converter.DomainToReport.toReportArbitrationManager
import com.example.newsletters.converter.DomainToReport.toReportDebtor
import com.example.newsletters.converter.DomainToReport.toReportQueue
import com.example.newsletters.entity.Document
import com.example.newsletters.entity.enum.QueueType.FIRST
import com.example.newsletters.entity.enum.QueueType.SECOND
import com.example.newsletters.entity.enum.QueueType.THIRD_DEPOSIT
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.QueueRepository
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils.getFile
import com.example.newsletters.dto.report.Debtor as ReportDebtor

private const val FIRST_QUEUE_DS = "FIRST_QUEUE_DS"
private const val SECOND_QUEUE_DS = "SECOND_QUEUE_DS"
private const val THIRD_DEPOSIT_QUEUE_DS = "THIRD_DEPOSIT_QUEUE_DS"

private const val FIRST_QUEUE_EXIGENCE_DS = "FIRST_QUEUE_EXIGENCE_DS"
private const val SECOND_QUEUE_EXIGENCE_DS = "SECOND_QUEUE_EXIGENCE_DS"
private const val THIRD_DEPOSIT_QUEUE_EXIGENCE_DS = "THIRD_DEPOSIT_QUEUE_EXIGENCE_DS"
private const val THIRD_DEPOSIT_QUEUE_REPAYMENT_DS = "THIRD_DEPOSIT_QUEUE_REPAYMENT_DS"

private const val FIRST_QUEUE_REPAYMENT_DS = "FIRST_QUEUE_REPAYMENT_DS"
private const val SECOND_QUEUE_REPAYMENT_DS = "SECOND_QUEUE_REPAYMENT_DS"

private const val THIRD_DEPOSIT_QUEUE_OBLIGATION_DS = "THIRD_DEPOSIT_QUEUE_OBLIGATION_DS"

private const val CREDITORS_FIRST_QUEUE_INFO = "creditors_first_queue_info"
private const val CREDITORS_FIRST_QUEUE_EXIGENCE_INFO = "creditors_first_queue_exigence_info"
private const val CREDITORS_FIRST_QUEUE_REPAYMENT_INFO = "creditors_first_queue_repayment_info"

private const val CREDITORS_SECOND_QUEUE_INFO = "creditors_second_queue_info"
private const val CREDITORS_SECOND_QUEUE_EXIGENCE_INFO = "creditors_second_queue_exigence_info"
private const val CREDITORS_SECOND_QUEUE_REPAYMENT_INFO = "creditors_second_queue_repayment_info"

private const val CREDITORS_THIRD_DEPOSIT_QUEUE_INFO = "creditors_third_deposit_queue_info"
private const val CREDITORS_THIRD_DEPOSIT_QUEUE_EXIGENCE_INFO = "creditors_third_deposit_queue_exigence_info"
private const val CREDITORS_THIRD_DEPOSIT_QUEUE_OBLIGATION_INFO = "creditors_third_deposit_queue_obligation_info"
private const val CREDITORS_THIRD_DEPOSIT_QUEUE_REPAYMENT_INFO = "creditors_third_deposit_queue_repayment_info"

@Service
class ReestrDataService(
    private val debtorRepository: DebtorRepository,
    private val creditorRepository: CreditorRepository,
    private val queueRepository: QueueRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository
): JasperDataService {
    override fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> = run {
        val debtorId = document.debtorId
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val arbitrationManager = debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val creditors = creditorRepository.findByDebtorId(debtorId)
        val queues = creditors.flatMap { queueRepository.findByCreditorId(it.id!!) }
        val firstQueues = queues.filter { it.type == FIRST }.map { it.toReportQueue() }
        val secondQueues = queues.filter { it.type == SECOND }.map { it.toReportQueue() }
        val thirdDepositQueues = queues.filter { it.type == THIRD_DEPOSIT }.map { it.toReportQueue() }
        val creditorFirstQueueInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_FIRST_QUEUE_INFO.jrxml").absolutePath)
        val creditorFirstQueueExigenceInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_FIRST_QUEUE_EXIGENCE_INFO.jrxml").absolutePath)
        val creditorsFirstQueueRepaymentInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_FIRST_QUEUE_REPAYMENT_INFO.jrxml").absolutePath)

        val creditorSecondQueueInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_SECOND_QUEUE_INFO.jrxml").absolutePath)
        val creditorSecondQueueExigenceInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_SECOND_QUEUE_EXIGENCE_INFO.jrxml").absolutePath)
        val creditorsSecondQueueRepaymentInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_SECOND_QUEUE_REPAYMENT_INFO.jrxml").absolutePath)

        val creditorThirdDepositQueueInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_THIRD_DEPOSIT_QUEUE_INFO.jrxml").absolutePath)
        val creditorThirdDepositQueueExigenceInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_THIRD_DEPOSIT_QUEUE_EXIGENCE_INFO.jrxml").absolutePath)
        val creditorsThirdDepositQueueObligationInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_THIRD_DEPOSIT_QUEUE_OBLIGATION_INFO.jrxml").absolutePath)
        val creditorsThirdDepositQueueRepaymentInfo = JasperCompileManager.compileReport(getFile("report/reestr/$CREDITORS_THIRD_DEPOSIT_QUEUE_REPAYMENT_INFO.jrxml").absolutePath)

        val params = hashMapOf(
            DEBTOR to debtor?.toReportDebtor(),
            ARBITRATION_MANAGER to arbitrationManager?.toReportArbitrationManager(),
            FIRST_QUEUE_DS to JRBeanCollectionDataSource(firstQueues),
            FIRST_QUEUE_EXIGENCE_DS to JRBeanCollectionDataSource(firstQueues),
            FIRST_QUEUE_REPAYMENT_DS to JRBeanCollectionDataSource(firstQueues),
            SECOND_QUEUE_DS to JRBeanCollectionDataSource(secondQueues),
            SECOND_QUEUE_EXIGENCE_DS to JRBeanCollectionDataSource(secondQueues),
            SECOND_QUEUE_REPAYMENT_DS to JRBeanCollectionDataSource(secondQueues),
            THIRD_DEPOSIT_QUEUE_DS to JRBeanCollectionDataSource(thirdDepositQueues),
            THIRD_DEPOSIT_QUEUE_EXIGENCE_DS to JRBeanCollectionDataSource(thirdDepositQueues),
            THIRD_DEPOSIT_QUEUE_OBLIGATION_DS to JRBeanCollectionDataSource(thirdDepositQueues),
            THIRD_DEPOSIT_QUEUE_REPAYMENT_DS to JRBeanCollectionDataSource(thirdDepositQueues),
            "$CREDITORS_FIRST_QUEUE_INFO.jasper" to creditorFirstQueueInfo,
            "$CREDITORS_FIRST_QUEUE_EXIGENCE_INFO.jasper" to creditorFirstQueueExigenceInfo,
            "$CREDITORS_FIRST_QUEUE_REPAYMENT_INFO.jasper" to creditorsFirstQueueRepaymentInfo,
            "$CREDITORS_SECOND_QUEUE_INFO.jasper" to creditorSecondQueueInfo,
            "$CREDITORS_SECOND_QUEUE_EXIGENCE_INFO.jasper" to creditorSecondQueueExigenceInfo,
            "$CREDITORS_SECOND_QUEUE_REPAYMENT_INFO.jasper" to creditorsSecondQueueRepaymentInfo,
            "$CREDITORS_THIRD_DEPOSIT_QUEUE_INFO.jasper" to creditorThirdDepositQueueInfo,
            "$CREDITORS_THIRD_DEPOSIT_QUEUE_EXIGENCE_INFO.jasper" to creditorThirdDepositQueueExigenceInfo,
            "$CREDITORS_THIRD_DEPOSIT_QUEUE_OBLIGATION_INFO.jasper" to creditorsThirdDepositQueueObligationInfo,
            "$CREDITORS_THIRD_DEPOSIT_QUEUE_REPAYMENT_INFO.jasper" to creditorsThirdDepositQueueRepaymentInfo
        )
        val source = JRBeanCollectionDataSource(debtor?.toReportDebtor()?.let { listOf(it) } ?: listOf<ReportDebtor>())
        source to params
    }
}

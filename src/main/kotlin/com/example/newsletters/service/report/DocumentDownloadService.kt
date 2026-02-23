package com.example.newsletters.service.report

import com.example.newsletters.entity.Document
import com.example.newsletters.entity.enum.DocumentTemplateType.BULLETIN
import com.example.newsletters.entity.enum.DocumentTemplateType.CREDITOR_NOTIFICATION
import com.example.newsletters.entity.enum.DocumentTemplateType.REESTR
import com.example.newsletters.entity.enum.DocumentTemplateType.REGISTRATION_CREDITOR_JOURNAL
import com.example.newsletters.entity.enum.DocumentTemplateType.REGISTRATION_WORKER_JOURNAL
import com.example.newsletters.entity.enum.DocumentTemplateType.REQUEST
import com.example.newsletters.entity.enum.DocumentTemplateType.WORKER_NOTIFICATION
import com.example.newsletters.repository.DocumentRepository
import com.example.newsletters.service.report.jasper.BulletinDataService
import com.example.newsletters.service.report.jasper.CreditorNotificationDataService
import com.example.newsletters.service.report.jasper.ReestrDataService
import com.example.newsletters.service.report.jasper.RegistrationCreditorJournalDataService
import com.example.newsletters.service.report.jasper.RegistrationWorkerJournalDataService
import com.example.newsletters.service.report.jasper.RequestDataService
import com.example.newsletters.service.report.jasper.WorkerNotificationDataService
import net.sf.jasperreports.engine.JRParameter.REPORT_LOCALE
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.springframework.context.i18n.LocaleContextHolder.getLocale
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.ByteArrayOutputStream
import kotlin.collections.plus

@Service
class DocumentDownloadService(
    private val documentRepository: DocumentRepository,
    private val bulletinDataService: BulletinDataService,
    private val workerNotificationDataService: WorkerNotificationDataService,
    private val creditorNotificationDataService: CreditorNotificationDataService,
    private val requestDataService: RequestDataService,
    private val registrationCreditorJournalDataService: RegistrationCreditorJournalDataService,
    private val registrationWorkerJournalDataService: RegistrationWorkerJournalDataService,
    private val reestrDataService: ReestrDataService,
) {
    /**
     * Загружает документ по указанному идентификатору документа
     *
     * @param documentId Идентификатор документа для загрузки.
     * @return Массив байтов содержимого документа
     * */
    fun download(documentId: Long): ByteArray? =
        documentRepository.findById(documentId)
            .filter { document -> !document.templateResourcePath.isNullOrEmpty() }
            .map { document ->
                val file = ResourceUtils.getFile(document.templateResourcePath.orEmpty())
                val jasperReport = JasperCompileManager.compileReport(file.absolutePath)
                var (dataSource, parameters) = getReportData(document)
                parameters = parameters.plus(REPORT_LOCALE to getLocale())
                val jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource)
                val exporter = JRDocxExporter()
                val baos = ByteArrayOutputStream()
                exporter.setExporterInput(SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(SimpleOutputStreamExporterOutput(baos))
                exporter.exportReport()
                baos.toByteArray()
            }.orElse(null)

    private fun getReportData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> =
        when (document.type) {
            BULLETIN -> bulletinDataService.getReportData(document)
            WORKER_NOTIFICATION -> workerNotificationDataService.getReportData(document)
            CREDITOR_NOTIFICATION -> creditorNotificationDataService.getReportData(document)
            REQUEST -> requestDataService.getReportData(document)
            REGISTRATION_CREDITOR_JOURNAL -> registrationCreditorJournalDataService.getReportData(document)
            REGISTRATION_WORKER_JOURNAL -> registrationWorkerJournalDataService.getReportData(document)
            REESTR -> reestrDataService.getReportData(document)
            else -> JRBeanCollectionDataSource(listOf<Any>()) to hashMapOf()
        }
}

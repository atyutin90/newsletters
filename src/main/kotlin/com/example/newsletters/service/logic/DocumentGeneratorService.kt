package com.example.newsletters.service.logic

import com.example.newsletters.dto.DocumentInfo
import com.example.newsletters.dto.helper.QueueHelper
import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.Debtor
import com.example.newsletters.entity.Document
import com.example.newsletters.entity.WorkerMeeting
import com.example.newsletters.entity.enum.DocumentTemplateType.REGISTRATION_CREDITOR_JOURNAL
import com.example.newsletters.entity.enum.DocumentTemplateType.BULLETIN
import com.example.newsletters.entity.enum.DocumentTemplateType.WORKER_NOTIFICATION
import com.example.newsletters.entity.enum.DocumentTemplateType.CREDITOR_NOTIFICATION
import com.example.newsletters.entity.enum.DocumentTemplateType.REGISTRATION_WORKER_JOURNAL
import com.example.newsletters.entity.enum.DocumentTemplateType.REQUEST
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorMeetingQuestionRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.DocumentRepository
import com.example.newsletters.repository.DocumentTemplateRepository
import com.example.newsletters.repository.QuestionRepository
import com.example.newsletters.repository.QueueRepository
import com.example.newsletters.repository.RequestRepository
import com.example.newsletters.repository.WorkerMeetingParticipantRepository
import com.example.newsletters.repository.WorkerMeetingRepository
import jakarta.transaction.Transactional
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.ByteArrayOutputStream
import kotlin.Long.Companion.MIN_VALUE

private const val ARBITRATION_MANAGER = "arbitrationManager"
private const val DEBTOR = "debtor"
private const val DEBTOR_MEETING = "debtorMeeting"
private const val QUEUES = "queues"
private const val QUEUE_HELPER = "queueHelper"
private const val REQUEST_PARAM = "request"
private const val WORKER_MEETING = "workerMeeting"

@Service
class DocumentGeneratorService(
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val documentRepository: DocumentRepository,
    private val debtorRepository: DebtorRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val documentTemplateRepository: DocumentTemplateRepository,
    private val creditorRepository: CreditorRepository,
    private val debtorMeetingQuestionRepository: DebtorMeetingQuestionRepository,
    private val questionRepository: QuestionRepository,
    private val queueRepository: QueueRepository,
    private val workerMeetingRepository: WorkerMeetingRepository,
    private val requestRepository: RequestRepository,
    private val workerMeetingParticipantRepository: WorkerMeetingParticipantRepository
) {

    /**
     * Генерирует документы для заданного должника.
     *
     * @param debtorId Идентификатор должника, для которого необходимо сгенерировать документы
     */
    @Transactional
    fun generateDocument(debtorId: Long) {
        clear(debtorId)
        if (existDebtorMeeting(debtorId)) {
            generateBulletins(debtorId)
            generateRegistrationCreditorJournal(debtorId)
            generateCreditorNotification(debtorId)
        }
        if (existWorkerMeeting(debtorId)) {
            generateRegistrationWorkerJournal(debtorId)
            generateWorkerNotification(debtorId)
        }
        if (existRequests(debtorId)) {
            generateRequests(debtorId)
        }
    }

    /**
     * Загружает документ по указанному идентификатору документа
     *
     * @param documentId Идентификатор документа для загрузки.
     * @return Массив байтов содержимого документа
     * */
    fun downloadDocument(documentId: Long): ByteArray? =
        documentRepository.findById(documentId)
            .filter { document -> !document.templateResourcePath.isNullOrEmpty() }
            .map { document ->
                val file = ResourceUtils.getFile(document.templateResourcePath.orEmpty())
                val jasperReport = JasperCompileManager.compileReport(file.absolutePath)

                val (dataSource, parameters) = when (document.type) {
                    BULLETIN -> getBulletinData(document)
                    WORKER_NOTIFICATION -> getWorkerNotificationData(document)
                    CREDITOR_NOTIFICATION -> getCreditorNotificationData(document)
                    REGISTRATION_CREDITOR_JOURNAL -> getRegistrationCreditorJournalData(document)
                    REGISTRATION_WORKER_JOURNAL -> getRegistrationWorkerJournalData(document)
                    REQUEST -> getRequestData(document)
                    else -> JRBeanCollectionDataSource(listOf<Any>()) to hashMapOf()
                }
                val jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource)
                val exporter = JRDocxExporter()
                val baos = ByteArrayOutputStream()
                exporter.setExporterInput(SimpleExporterInput(jasperPrint))
                exporter.setExporterOutput(SimpleOutputStreamExporterOutput(baos))
                exporter.exportReport()
                baos.toByteArray()
            }.orElse(null)

    private fun getRegistrationCreditorJournalData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> =
        run {
            val debtorId = document.debtorId
            val creditors = creditorRepository.findByDebtorId(debtorId)
            val debtor = debtorRepository.findById(debtorId).orElse(null)
            val debtorMeeting = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()
            val arbitrationManager =
                debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
            val queues = queueRepository.findByCreditorIds(creditors.mapNotNull { it.id })

            val dataSource = JRBeanCollectionDataSource(creditors)
            val parameters = hashMapOf(
                DEBTOR to debtor,
                DEBTOR_MEETING to debtorMeeting,
                ARBITRATION_MANAGER to arbitrationManager,
                QUEUES to queues,
                QUEUE_HELPER to QueueHelper()

            )
            dataSource to parameters
        }

    private fun getRegistrationWorkerJournalData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> =
        run {
            val debtorId = document.debtorId
            val debtor = debtorRepository.findById(debtorId).orElse(null)
            val workerMeeting = workerMeetingRepository.findByDebtorId(debtorId).firstOrNull()
            val workerMeetingParticipants =
                workerMeeting?.id?.let { workerMeetingParticipantRepository.findByWorkerMeetingId(it) } ?: listOf()
            val arbitrationManager =
                debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
            val dataSource = JRBeanCollectionDataSource(workerMeetingParticipants)
            val parameters = hashMapOf(
                DEBTOR to debtor,
                WORKER_MEETING to workerMeeting,
                ARBITRATION_MANAGER to arbitrationManager,
            )
            dataSource to parameters
        }

    private fun getRequestData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> = run {
        val debtorId = document.debtorId
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val arbitrationManager =
            debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val request = document.info?.requestId?.let { requestId -> requestRepository.findById(requestId).orElse(null) }
        val parameters = hashMapOf(
            DEBTOR to debtor,
            REQUEST_PARAM to request,
            ARBITRATION_MANAGER to arbitrationManager,
        )
        val dataSource = JRBeanCollectionDataSource(debtor?.let { listOf(it) } ?: listOf<Debtor>())
        dataSource to parameters
    }

    private fun getBulletinData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> = run {
        val debtorId = document.debtorId
        val creditor = document.info?.creditorId?.let { creditorRepository.findById(it).orElse(null) }
        val debtor = debtorRepository.findById(debtorId).orElse(null)
        val debtorMeeting = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()
        val arbitrationManager =
            debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
        val queues = creditor?.id?.let { queueRepository.findByCreditorId(it) } ?: listOf()

        val dataSource = JRBeanCollectionDataSource(creditor?.let { listOf(it) } ?: listOf<Creditor>())
        val parameters = hashMapOf(
            DEBTOR to debtor,
            DEBTOR_MEETING to debtorMeeting,
            ARBITRATION_MANAGER to arbitrationManager,
            QUEUES to queues,
            QUEUE_HELPER to QueueHelper()
        )
        dataSource to parameters
    }

    private fun getWorkerNotificationData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> =
        run {
            val debtorId = document.debtorId
            val debtor = debtorRepository.findById(debtorId).orElse(null)
            val workerMeeting = workerMeetingRepository.findByDebtorId(debtorId).firstOrNull()
            val arbitrationManager =
                debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
            val dataSource = JRBeanCollectionDataSource(workerMeeting?.let { listOf(it) } ?: listOf<WorkerMeeting>())
            val parameters = hashMapOf(
                DEBTOR to debtor,
                WORKER_MEETING to workerMeeting,
                ARBITRATION_MANAGER to arbitrationManager,
            )
            dataSource to parameters
        }

    private fun getCreditorNotificationData(document: Document): Pair<JRBeanCollectionDataSource, Map<String, Any?>> =
        run {
            val debtorId = document.debtorId
            val creditor = document.info?.creditorId?.let { creditorRepository.findById(it).orElse(null) }
            val debtor = debtorRepository.findById(debtorId).orElse(null)
            val debtorMeeting = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()
            val arbitrationManager =
                debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }
            val queues = creditor?.id?.let { queueRepository.findByCreditorId(it) } ?: listOf()

            val dataSource = JRBeanCollectionDataSource(creditor?.let { listOf(it) } ?: listOf<Creditor>())
            val parameters = hashMapOf(
                DEBTOR to debtor,
                DEBTOR_MEETING to debtorMeeting,
                ARBITRATION_MANAGER to arbitrationManager,
                QUEUES to queues
            )
            dataSource to parameters
        }


    private fun clear(debtorId: Long) {
        documentRepository.deleteByDebtorId(debtorId)
    }

    private fun generateBulletins(debtorId: Long) {
        val questions = questionRepository.findAll()
        val positionTemplateMap =
            questions.associate { it -> it.position to documentTemplateRepository.findAllById(it.documentTemplateIds) }
        val documentTemplates = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()?.let { debtorMeeting ->
            debtorMeetingQuestionRepository.findByDebtorMeetingId(debtorMeeting.id ?: MIN_VALUE)
                .mapNotNull { dmq -> dmq.position?.let { positionTemplateMap[it] } }
                .flatMap { it }
                .mapNotNull { it }
        } ?: listOf()

        creditorRepository.findByDebtorId(debtorId).forEach { creditor ->
            documentTemplates.forEach { dt ->
                val document = Document(
                    debtorId = debtorId,
                    type = dt.type,
                    name = "${creditor.name}_${dt.name}",
                    templateResourcePath = dt.resourcePath,
                    info = DocumentInfo(creditorId = creditor.id)
                )
                documentRepository.save(document)
            }
        }
    }

    private fun generateRegistrationCreditorJournal(debtorId: Long) {
        documentTemplateRepository.findByType(REGISTRATION_CREDITOR_JOURNAL).firstOrNull()?.let { dt ->
            documentRepository.save(
                Document(
                    debtorId = debtorId,
                    name = dt.name,
                    type = REGISTRATION_CREDITOR_JOURNAL,
                    templateResourcePath = dt.resourcePath
                )
            )
        }
    }

    private fun generateCreditorNotification(debtorId: Long) {
        creditorRepository.findByDebtorId(debtorId).forEach { creditor ->
            documentTemplateRepository.findByType(CREDITOR_NOTIFICATION).firstOrNull()?.let { dt ->
                documentRepository.save(
                    Document(
                        debtorId = debtorId,
                        name = "${creditor.name}_${dt.name}",
                        type = CREDITOR_NOTIFICATION,
                        templateResourcePath = dt.resourcePath,
                        info = DocumentInfo(creditorId = creditor.id)
                    )
                )
            }
        }

    }

    private fun generateWorkerNotification(debtorId: Long) {
        documentTemplateRepository.findByType(WORKER_NOTIFICATION).firstOrNull()?.let { dt ->
            documentRepository.save(
                Document(
                    debtorId = debtorId,
                    name = dt.name,
                    type = WORKER_NOTIFICATION,
                    templateResourcePath = dt.resourcePath
                )
            )
        }
    }

    private fun generateRegistrationWorkerJournal(debtorId: Long) {
        documentTemplateRepository.findByType(REGISTRATION_WORKER_JOURNAL).firstOrNull()?.let { dt ->
            documentRepository.save(
                Document(
                    debtorId = debtorId,
                    name = dt.name,
                    type = REGISTRATION_WORKER_JOURNAL,
                    templateResourcePath = dt.resourcePath
                )
            )
        }
    }

    private fun generateRequests(debtorId: Long) {
        requestRepository.findByDebtorId(debtorId).forEach { request ->
            request.destination?.documentTemplateId?.let { documentTemplateId ->
                documentTemplateRepository.findById(documentTemplateId)
                    .map { dt ->
                        documentRepository.save(
                            Document(
                                debtorId = debtorId,
                                name = request.destinationDetail?.let { "${dt.name} (${it})" } ?: dt.name,
                                type = REQUEST,
                                templateResourcePath = dt.resourcePath,
                                info = DocumentInfo(requestId = request.id)
                            )
                        )
                    }
            }
        }
    }


    private fun existDebtorMeeting(debtorId: Long): Boolean =
        debtorMeetingRepository.findByDebtorId(debtorId).isNotEmpty()

    private fun existWorkerMeeting(debtorId: Long): Boolean =
        workerMeetingRepository.findByDebtorId(debtorId).isNotEmpty()

    private fun existRequests(debtorId: Long): Boolean = requestRepository.findByDebtorId(debtorId).isNotEmpty()
}
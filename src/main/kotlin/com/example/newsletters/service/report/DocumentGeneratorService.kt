package com.example.newsletters.service.report

import com.example.newsletters.entity.Document
import com.example.newsletters.entity.DocumentInfo
import com.example.newsletters.entity.enum.DocumentTemplateType.CREDITOR_NOTIFICATION
import com.example.newsletters.entity.enum.DocumentTemplateType.REESTR
import com.example.newsletters.entity.enum.DocumentTemplateType.REGISTRATION_CREDITOR_JOURNAL
import com.example.newsletters.entity.enum.DocumentTemplateType.REGISTRATION_WORKER_JOURNAL
import com.example.newsletters.entity.enum.DocumentTemplateType.REQUEST
import com.example.newsletters.entity.enum.DocumentTemplateType.WORKER_NOTIFICATION
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorMeetingQuestionRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DocumentRepository
import com.example.newsletters.repository.DocumentTemplateRepository
import com.example.newsletters.repository.QuestionRepository
import com.example.newsletters.repository.RequestRepository
import com.example.newsletters.repository.WorkerMeetingRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import kotlin.Long.Companion.MIN_VALUE

@Service
class DocumentGeneratorService(
    private val documentRepository: DocumentRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val requestRepository: RequestRepository,
    private val workerMeetingRepository: WorkerMeetingRepository,
    private val documentTemplateRepository: DocumentTemplateRepository,
    private val questionRepository: QuestionRepository,
    private val debtorMeetingQuestionRepository: DebtorMeetingQuestionRepository,
    private val creditorRepository: CreditorRepository,
) {
    /**
     * Генерирует документы для заданного должника.
     *
     * @param debtorId идентификатор должника, для которого необходимо сгенерировать документы
     */
    @Transactional
    fun generate(debtorId: Long) {
        clear(debtorId)
        if (existDebtorMeeting(debtorId)) {
            generateBulletins(debtorId)
            generateRegistrationCreditorJournal(debtorId)
            generateCreditorNotification(debtorId)
            generateReestr(debtorId)
        }
        if (existWorkerMeeting(debtorId)) {
            generateRegistrationWorkerJournal(debtorId)
            generateWorkerNotification(debtorId)
        }
        if (existRequests(debtorId)) {
            generateRequests(debtorId)
        }
    }

    private fun clear(debtorId: Long) {
        documentRepository.deleteByDebtorId(debtorId)
    }

    private fun existDebtorMeeting(debtorId: Long): Boolean =
        debtorMeetingRepository.findByDebtorId(debtorId).isNotEmpty()

    private fun existWorkerMeeting(debtorId: Long): Boolean =
        workerMeetingRepository.findByDebtorId(debtorId).isNotEmpty()

    private fun existRequests(debtorId: Long): Boolean =
        requestRepository.findByDebtorId(debtorId).isNotEmpty()

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

    //TODO
    private fun generateBulletins(debtorId: Long) {
        val questions = questionRepository.findAll()
        val positionTemplateMap = questions.associate { it.position to documentTemplateRepository.findAllById(it.documentTemplateIds) }
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
        val creditors = creditorRepository.findByDebtorId(debtorId)
        if (creditors.isNotEmpty()) {
            documentTemplateRepository.findByType(CREDITOR_NOTIFICATION).firstOrNull()?.let { dt ->
                documentRepository.save(
                    Document(
                        debtorId = debtorId,
                        name = "${dt.name}",
                        type = CREDITOR_NOTIFICATION,
                        templateResourcePath = dt.resourcePath
                    )
                )
            }
        }
    }

    private fun generateReestr(debtorId: Long) {
        val creditors = creditorRepository.findByDebtorId(debtorId)
        if (creditors.isNotEmpty()) {
            documentTemplateRepository.findByType(REESTR).firstOrNull()?.let { dt ->
                documentRepository.save(
                    Document(
                        debtorId = debtorId,
                        name = "${dt.name}",
                        type = REESTR,
                        templateResourcePath = dt.resourcePath
                    )
                )
            }
        }
    }
}
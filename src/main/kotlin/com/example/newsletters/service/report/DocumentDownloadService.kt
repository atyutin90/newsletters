package com.example.newsletters.service.report

import com.example.newsletters.entity.Document
import com.example.newsletters.entity.enum.DocumentTemplateType.BULLETIN
import com.example.newsletters.entity.enum.DocumentTemplateType.CREDITOR_NOTIFICATION
import com.example.newsletters.entity.enum.DocumentTemplateType.PROTOCOL
import com.example.newsletters.entity.enum.DocumentTemplateType.REESTR
import com.example.newsletters.entity.enum.DocumentTemplateType.REGISTRATION_CREDITOR_JOURNAL
import com.example.newsletters.entity.enum.DocumentTemplateType.REGISTRATION_WORKER_JOURNAL
import com.example.newsletters.entity.enum.DocumentTemplateType.REQUEST
import com.example.newsletters.entity.enum.DocumentTemplateType.WORKER_NOTIFICATION
import com.example.newsletters.exceptions.DocumentTypeNotFoundException
import com.example.newsletters.repository.DocumentRepository
import com.example.newsletters.service.report.poi.BulletinPoiDocumentService
import com.example.newsletters.service.report.poi.CreditorNotificationPoiService
import com.example.newsletters.service.report.poi.MeetingParticipantsRegistrationJournalPoiService
import com.example.newsletters.service.report.poi.ProtocolPoiService
import com.example.newsletters.service.report.poi.ReestrPoiDocumentService
import com.example.newsletters.service.report.poi.RegistrationWorkerJournalPoiService
import com.example.newsletters.service.report.poi.RequestPoiDocumentService
import com.example.newsletters.service.report.poi.WorkerNotificationPoiService
import org.springframework.stereotype.Service

@Service
class DocumentDownloadService(
    private val documentRepository: DocumentRepository,
    private val bulletinPoiDocumentService: BulletinPoiDocumentService,
    private val creditorNotificationPoiService: CreditorNotificationPoiService,
    private val meetingParticipantsRegistrationJournalPoiService: MeetingParticipantsRegistrationJournalPoiService,
    private val registrationWorkerJournalPoiService: RegistrationWorkerJournalPoiService,
    private val reestrPoiDocumentService: ReestrPoiDocumentService,
    private val requestPoiDocumentService: RequestPoiDocumentService,
    private val workerNotificationPoiService: WorkerNotificationPoiService,
    private val protocolPoiService: ProtocolPoiService
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
            .map { document -> generate(document) }
            .orElse(null)

    private fun generate(document: Document): ByteArray =
        when (document.type) {
            BULLETIN -> bulletinPoiDocumentService.generate(document)
            CREDITOR_NOTIFICATION -> creditorNotificationPoiService.generate(document)
            REGISTRATION_CREDITOR_JOURNAL -> meetingParticipantsRegistrationJournalPoiService.generate(document)
            REGISTRATION_WORKER_JOURNAL -> registrationWorkerJournalPoiService.generate(document)
            REESTR -> reestrPoiDocumentService.generate(document)
            REQUEST -> requestPoiDocumentService.generate(document)
            WORKER_NOTIFICATION -> workerNotificationPoiService.generate(document)
            PROTOCOL -> protocolPoiService.generate(document)
            else -> throw DocumentTypeNotFoundException(document.type)
        }
}

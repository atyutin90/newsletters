package com.example.newsletters.service

import com.example.newsletters.entity.ArbitrationManager
import com.example.newsletters.entity.Creditor
import com.example.newsletters.entity.Debtor
import com.example.newsletters.entity.DebtorMeeting
import com.example.newsletters.entity.Document
import com.example.newsletters.entity.enum.DocumentTemplateType
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.CreditorRepository
import com.example.newsletters.repository.DebtorMeetingQuestionRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.DocumentRepository
import com.example.newsletters.repository.DocumentTemplateRepository
import com.example.newsletters.repository.QuestionRepository
import com.example.newsletters.repository.QueueRepository
import com.example.newsletters.utils.FileTemplateUtils.generateTemplate
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

@Service
class DocumentService(
    private val debtorRepository: DebtorRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val debtorMeetingQuestionRepository: DebtorMeetingQuestionRepository,
    private val questionRepository: QuestionRepository,
    private val documentTemplateRepository: DocumentTemplateRepository,
    private val documentRepository: DocumentRepository,
    private val creditorRepository: CreditorRepository,
    private val queueRepository: QueueRepository
) {

    /*fun generateDocument()

    fun generate(debtorId: Long) {
        clear(debtorId)
        //generateBulletins(debtorId)
        //generateDebtorMeetingJournal(debtorId)
    }

    private fun clear(debtorId: Long) {
        documentRepository.deleteByDebtorId(debtorId)
    }*/

   /* private fun generateBulletins(debtorId: Long) {
        val debtorMeeting = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()
        val debtor = debtorRepository.findById(debtorId).get()
        val creditors = creditorRepository.findByDebtorId(debtorId)
        val questions = questionRepository.findAll()
        val arbitrationManager =
            debtor.arbitrationManagerId?.let { arbitrationManagerRepository.findById(it).orElse(null) }

        val positionTemplateMap =
            questions.associate { it -> it.position to documentTemplateRepository.findAllById(it.documentTemplateIds) }

        creditors.forEach { creditor ->
            val queues = queueRepository.findByCreditorId(creditor.id!!)
            debtorMeeting?.let { dm ->
                val documentTemplates = debtorMeetingQuestionRepository.findByDebtorMeetingId(dm.id!!)
                    .mapNotNull { dmq -> dmq.position?.let { positionTemplateMap[it] } }
                    .flatMap { it }
                    .filter { it.data != null }

                val map = mapObject(creditor)
                    .plus(mapOf("queues" to queues))
                    .plus(mapObject(debtor))
                    .plus(mapObject(debtorMeeting))
                    .let {
                        if (arbitrationManager != null) {
                            it.plus(mapObject(arbitrationManager))
                        } else it
                    }

                documentTemplates.forEach { dt ->
                    val outputStream = generateTemplate(ByteArrayInputStream(dt.data), map)
                    //val load = WordprocessingMLPackage.load(ByteArrayInputStream(dt.data))
                    //val outputStream = generateTemplate(load, map)
                    val document = Document(
                        debtorId = dm.debtorId,
                        type = dt.type,
                        contentType = dt.contentType,
                        name = "${creditor.name}_${dt.name}",
                        fileName = "${creditor.name}_${dt.fileName}",
                        data = outputStream.toByteArray()
                    )
                    documentRepository.save(document)
                }
            }
        }

    }

    private fun generateFirstNotification(debtorId: Long) {
    }

    private fun generateSecondNotification(debtorId: Long) {
    }

    private fun generateDebtorMeetingJournal(debtorId: Long) {
        val template =
            documentTemplateRepository.findByType(DocumentTemplateType.REGISTRATION_CREDITOR_JOURNAL).firstOrNull()

        val debtorMeeting = debtorMeetingRepository.findByDebtorId(debtorId).firstOrNull()
        val debtor = debtorRepository.findById(debtorId).get()
        val creditors = creditorRepository.findByDebtorId(debtorId)

        template?.let { dt ->
            if (dt.data != null) {
                debtorMeeting?.let { dm ->
                    val map = mapObject(debtor)
                        .plus(mapObject(debtor))
                        .plus(mapObject(dm))
                        .plus(mapOf("creditors" to creditors))

                    val outputStream = generateTemplate(ByteArrayInputStream(dt.data), map)
                    val document = Document(
                        debtorId = dm.debtorId,
                        type = dt.type,
                        contentType = dt.contentType,
                        name = "${dt.name}",
                        fileName = "${dt.fileName}",
                        data = outputStream.toByteArray()
                    )
                    documentRepository.save(document)
                }
            }
        }
    }*/


    private fun <T> mapObject(value: T): Map<String, Any> = when (value) {
        is ArbitrationManager -> mapOf("arbitrationManager" to value)
        is DebtorMeeting -> mapOf("debtorMeeting" to value)
        is Debtor -> mapOf("debtor" to value)
        is Creditor -> mapOf("creditor" to value)
        else -> mapOf()
    }
}

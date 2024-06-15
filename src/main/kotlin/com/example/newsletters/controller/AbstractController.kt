package com.example.newsletters.controller

import org.springframework.context.MessageSource
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*


const val KEYWORD = "keyword"
const val CREDITOR = "creditor"
const val CREDITORS = "creditors"
const val QUEUE = "queue"
const val QUEUES = "queues"
const val CLIENT_TYPES = "clientTypes"
const val DOCUMENT_TEMPLATE_TYPES = "documentTemplateTypes"
const val QUEUE_TYPES = "queueTypes"
const val REQUEST = "request"
const val REQUEST_ID = "requestId"
const val CREDITOR_ID = "creditorId"
const val QUEUE_ID = "queueId"
const val DEBTOR_ID = "debtorId"
const val REQUESTS = "requests"
const val PUBLICATION = "publication"
const val PUBLICATION_ID = "publicationId"
const val PUBLICATIONS = "publications"
const val DEBTOR_MEETING = "debtorMeeting"
const val DEBTOR_MEETINGS = "debtorMeetings"
const val DEBTOR_MEETING_ID = "debtorMeetingId"
const val DEBTOR_MEETING_QUESTION_ID = "debtorMeetingQuestionId"
const val DEBTOR_MEETING_QUESTION = "debtorMeetingQuestion"
const val DEBTOR_MEETING_QUESTIONS = "debtorMeetingQuestions"
const val DEBTOR_MEETING_PARTICIPANT = "debtorMeetingParticipant"
const val DEBTOR_MEETING_PARTICIPANTS = "debtorMeetingParticipants"
const val DEBTOR_MEETING_PARTICIPANT_ID = "debtorMeetingParticipantId"
const val DOCUMENT_ID = "documentId"
const val WORKER_MEETING = "workerMeeting"
const val WORKER_MEETINGS = "workerMeetings"
const val WORKER_MEETING_PARTICIPANT = "workerMeetingParticipant"
const val WORKER_MEETING_PARTICIPANTS = "workerMeetingParticipants"
const val WORKER_MEETING_ID = "workerMeetingId"
const val WORKER_MEETING_PARTICIPANT_ID = "workerMeetingParticipantId"
const val DATA = "data"
const val DOCUMENT_TEMPLATES = "documentTemplates"
const val REQUEST_DESTINATION = "requestDestination"
const val REQUEST_DESTINATIONS = "requestDestinations"
const val ARBITRATION_MANAGERS = "arbitrationManagers"
const val DOCUMENTS = "documents"
const val MESSAGE = "message"
const val ERROR_MESSAGE = "errorMessage"
const val PAGE_TITLE = "pageTitle"
const val ID = "id"
const val DEBTOR = "debtor"
const val DEBTORS = "debtors"
const val FILES = "files"
const val REPORTS = "reports"
const val LOCATIONS = "locations"
const val LOCATION = "location"
const val NEWSLETTER = "newsletter"
const val CURRENT_PAGE = "currentPage"
const val TOTAL_ITEMS = "totalItems"
const val TOTAL_PAGES = "totalPages"
const val PAGE_SIZE = "pageSize"

const val DEFAULT_PAGE = "1"
const val DEFAULT_PAGE_SIZE = "10"
abstract class AbstractController(open val messageSource: MessageSource) {

    fun infoMessageCreateOrUpdateRecord(redirectAttributes: RedirectAttributes, isUpdate: Boolean) =
        redirectAttributes.addFlashAttribute(
            MESSAGE, messageSource.getMessage(
                if (isUpdate) "record-successfully-updated" else "record-successfully-created",
                arrayOf(),
                Locale.getDefault()
            )
        )

    fun infoMessageDeleteRecord(redirectAttributes: RedirectAttributes, id: Long) =
        redirectAttributes.addFlashAttribute(MESSAGE, messageSource.getMessage("record-successfully-deleted", arrayOf(id), Locale.getDefault()))

}

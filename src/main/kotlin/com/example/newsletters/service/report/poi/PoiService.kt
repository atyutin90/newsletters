package com.example.newsletters.service.report.poi

import com.example.newsletters.entity.Document
import org.springframework.context.i18n.LocaleContextHolder.getLocale
import java.time.format.DateTimeFormatter

const val DEBTOR_NAME = "debtorName"
const val DEBTOR_FULL_NAME = "debtorFullName"
const val DEBTOR_TIN = "debtorTIN"
const val DEBTOR_SRN = "debtorSRN"
const val DEBTOR_PSRN = "debtorPSRN"
const val DEBTOR_COURT_ACT = "debtorCourtAct"
const val DEBTOR_CASE_NUMBER = "debtorCaseNumber"
const val DEBTOR_ACT_DATE = "debtorActDate"
const val DEBTOR_RESOLUTION_DATE = "debtorResolutionDate"
const val DEBTOR_ADDRESS = "debtorAddress"
const val DEBTOR_DOCUMENT_NUMBER = "debtorDocumentNumber"
const val DEBTOR_MEETING_ADDRESS = "debtorMeetingAddress"
const val DEBTOR_MEETING_DATE = "debtorMeetingDate"
const val DEBTOR_MEETING_TIME = "debtorMeetingTime"

const val DEBTOR_MEETING_REGISTRATION_TIME_FROM = "debtorMeetingRegistrationTimeFrom"
const val DEBTOR_MEETING_REGISTRATION_TIME_TO = "debtorMeetingRegistrationTimeTo"
const val DEBTOR_MEETING_FAMILIARIZATION_DATE_FROM = "debtorMeetingFamiliarizationDateFrom"
const val DEBTOR_MEETING_FAMILIARIZATION_DATE_TO = "debtorMeetingFamiliarizationDateTo"

const val WORKER_MEETING_DATE = "workerMeetingDate"
const val WORKER_MEETING_TIME = "workerMeetingTime"
const val WORKER_MEETING_ADDRESS = "workerMeetingAddress"
const val WORKER_MEETING_REGISTRATION_TIME_FROM = "workerMeetingRegistrationTimeFrom"
const val WORKER_MEETING_REGISTRATION_TIME_TO = "workerMeetingRegistrationTimeTo"

const val MANAGER_EMAIL = "managerEmail"
const val MANAGER_TIN = "managerTIN"
const val MANAGER_PIPN = "managerPIPN"
const val MANAGER_NAME = "managerName"
const val MANAGER_SD = "managerSD"
const val MANAGER_CONTACTS = "managerContacts"
const val MANAGER_PHONE = "managerPhone"
const val MANAGER_POSTAL_ADDRESS = "managerPostalAddress"

const val REQUEST_NUMBER = "requestNumber"
const val REQUEST_DATE = "requestDate"
const val REQUEST_DATE_FROM = "requestDateFrom"
const val REQUEST_DATE_TO = "requestDateTo"
const val REQUEST_DESTINATION = "requestDestination"
const val REQUEST_DESTINATION_DETAIL = "requestDestinationDetail"
const val REQUEST_ADDRESS = "requestAddress"
const val REQUEST_ACCOUNT = "requestAccount"

const val CREDITOR_NAME = "creditorName"
const val CREDITOR_ADDRESS = "creditorAddress"
const val QUEUE_AMOUNT = "queueAmount"
const val ROUND_QUEUE_AMOUNT = "roundQueueAmount"
const val VOTE_AMOUNT = "voteAmount"
const val ROW_NUMBER = "rowNumber"

fun longDatePattern(): DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'г.'", getLocale())
fun shortDatePattern(): DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", getLocale())

interface PoiService {

    fun generate(document: Document): ByteArray

    fun value(name: String) = "{{$name}}"
}

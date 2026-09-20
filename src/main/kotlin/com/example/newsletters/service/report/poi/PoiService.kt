package com.example.newsletters.service.report.poi

import com.example.newsletters.entity.Document
import org.springframework.context.i18n.LocaleContextHolder.getLocale
import java.time.format.DateTimeFormatter

const val DEBTOR_NAME = "debtorName"
const val MEETING_ADDRESS = "meetingAddress"
const val MEETING_DATE = "meetingDate"
const val MANAGER_NAME = "managerName"
const val CREDITOR_NAME = "creditorName"
const val CREDITOR_ADDRESS = "creditorAddress"
const val QUEUE_AMOUNT = "queueAmount"
const val ROUND_QUEUE_AMOUNT = "roundQueueAmount"
const val VOTE_AMOUNT = "voteAmount"
const val SRO_DATA = "sroData"
const val ROW_NUMBER = "rowNumber"
fun longDatePattern(): DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'г.'", getLocale())
fun shortDatePattern(): DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", getLocale())

interface PoiService {

    fun generate(document: Document): ByteArray

    fun value(name: String) = "{{$name}}"
}

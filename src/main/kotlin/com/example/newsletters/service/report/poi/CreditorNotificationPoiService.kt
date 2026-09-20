package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DebtorRepository
import org.springframework.stereotype.Service

private const val DEBTOR_FULL_NAME = "debtorFullName"
private const val MANAGER_CONTACTS = "managerContacts"
private const val DOCUMENT_NUMBER = "documentNumber"
private const val COURT_ACT = "courtAct"
private const val ACT_DATE = "actDate"
private const val RESOLUTION_DATE = "resolutionDate"
private const val CASE_NUMBER = "caseNumber"
private const val DEBTOR_TIN = "debtorTin"
private const val DEBTOR_OGRN = "debtorOgrn"
private const val DEBTOR_ADDRESS = "debtorAddress"
private const val MANAGER_TIN = "managerTin"
private const val MANAGER_SNILS = "managerSnils"
private const val MEETING_TIME = "meetingTime"
private const val REGISTRATION_TIME_FROM = "registrationTimeFrom"
private const val REGISTRATION_TIME_TO = "registrationTimeTo"
private const val FAMILIARIZATION_DATE_FROM = "familiarizationDateFrom"
private const val FAMILIARIZATION_DATE_TO = "familiarizationDateTo"
private const val MANAGER_PHONE = "managerPhone"

@Service
class CreditorNotificationPoiService(
    private val debtorRepository: DebtorRepository,
    private val debtorMeetingRepository: DebtorMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val poiTemplateService: PoiTemplateService,
) : PoiService {

    override fun generate(document: Document): ByteArray {
        val debtor = debtorRepository.findById(document.debtorId)
            .orElseThrow { IllegalStateException("Не найден должник ${document.debtorId}") }
        val meeting = debtorMeetingRepository.findByDebtorId(document.debtorId).firstOrNull()
            ?: throw IllegalStateException("Не найдено собрание кредиторов должника ${document.debtorId}")
        val manager = debtor.arbitrationManagerId
            ?.let { arbitrationManagerRepository.findById(it).orElse(null) }
            ?: throw IllegalStateException("Не найден временный управляющий должника ${document.debtorId}")

        val values = mapOf(
            value(DEBTOR_NAME) to debtor.name.orEmpty(),
            value(DEBTOR_FULL_NAME) to debtor.fullName.orEmpty(),
            value(MANAGER_NAME) to manager.fullName.orEmpty(),
            value(MANAGER_CONTACTS) to listOf(
                manager.postalAddress,
                manager.email,
                manager.phone,
            ).mapNotNull { it?.takeIf(String::isNotBlank) }.joinToString(" "),
            value(DOCUMENT_NUMBER) to debtor.documentNumber.orEmpty(),
            value(MEETING_DATE) to meeting.date?.format(shortDatePattern()).orEmpty(),
            value(COURT_ACT) to debtor.courtAct.orEmpty(),
            value(ACT_DATE) to debtor.actDate?.format(shortDatePattern()).orEmpty(),
            value(RESOLUTION_DATE) to debtor.resolutionDate?.format(shortDatePattern()).orEmpty(),
            value(CASE_NUMBER) to debtor.caseNumber.orEmpty(),
            value(DEBTOR_TIN) to debtor.taxpayerIdentificationNumber.orEmpty(),
            value(DEBTOR_OGRN) to debtor.primaryStateRegistrationNumber.orEmpty(),
            value(DEBTOR_ADDRESS) to debtor.address.orEmpty(),
            value(MANAGER_TIN) to manager.taxpayerIdentificationNumber.orEmpty(),
            value(MANAGER_SNILS) to manager.personalInsurancePolicyNumber.orEmpty(),
            value(SRO_DATA) to manager.sroData.orEmpty(),
            value(MEETING_ADDRESS) to meeting.address.orEmpty(),
            value(MEETING_TIME) to meeting.time?.toString().orEmpty(),
            value(REGISTRATION_TIME_FROM) to meeting.registrationTimeFrom?.toString().orEmpty(),
            value(REGISTRATION_TIME_TO) to meeting.registrationTimeTo?.toString().orEmpty(),
            value(FAMILIARIZATION_DATE_FROM) to meeting.familiarizationDateFrom?.format(shortDatePattern()).orEmpty(),
            value(FAMILIARIZATION_DATE_TO) to meeting.familiarizationDateTo?.format(shortDatePattern()).orEmpty(),
            value(MANAGER_PHONE) to manager.phone.orEmpty(),
        )

        return poiTemplateService.generate(
            configuredPath = document.templateResourcePath!!,
            data = PoiReportData(values),
        )
    }
}

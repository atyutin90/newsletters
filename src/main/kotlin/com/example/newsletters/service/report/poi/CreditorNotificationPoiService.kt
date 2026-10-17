package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorMeetingRepository
import com.example.newsletters.repository.DebtorRepository
import org.springframework.stereotype.Service

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
            value(DEBTOR_DOCUMENT_NUMBER) to debtor.documentNumber.orEmpty(),
            value(DEBTOR_MEETING_DATE) to meeting.date?.format(shortDatePattern()).orEmpty(),
            value(DEBTOR_COURT_ACT) to debtor.courtAct.orEmpty(),
            value(DEBTOR_ACT_DATE) to debtor.actDate?.format(shortDatePattern()).orEmpty(),
            value(DEBTOR_RESOLUTION_DATE) to debtor.resolutionDate?.format(shortDatePattern()).orEmpty(),
            value(DEBTOR_CASE_NUMBER) to debtor.caseNumber.orEmpty(),
            value(DEBTOR_TIN) to debtor.taxpayerIdentificationNumber.orEmpty(),
            value(DEBTOR_PSRN) to debtor.primaryStateRegistrationNumber.orEmpty(),
            value(DEBTOR_ADDRESS) to debtor.address.orEmpty(),
            value(DEBTOR_MEETING_ADDRESS) to meeting.address.orEmpty(),
            value(DEBTOR_MEETING_TIME) to meeting.time?.toString().orEmpty(),
            value(DEBTOR_MEETING_REGISTRATION_TIME_FROM) to meeting.registrationTimeFrom?.toString().orEmpty(),
            value(DEBTOR_MEETING_REGISTRATION_TIME_TO) to meeting.registrationTimeTo?.toString().orEmpty(),
            value(DEBTOR_MEETING_FAMILIARIZATION_DATE_FROM) to meeting.familiarizationDateFrom?.format(shortDatePattern()).orEmpty(),
            value(DEBTOR_MEETING_FAMILIARIZATION_DATE_TO) to meeting.familiarizationDateTo?.format(shortDatePattern()).orEmpty(),
            value(MANAGER_TIN) to manager.taxpayerIdentificationNumber.orEmpty(),
            value(MANAGER_PIPN) to manager.personalInsurancePolicyNumber.orEmpty(),
            value(MANAGER_SD) to manager.sroData.orEmpty(),
            value(MANAGER_PHONE) to manager.phone.orEmpty()
        )

        return poiTemplateService.generate(
            configuredPath = document.templateResourcePath!!,
            data = PoiReportData(values),
        )
    }
}

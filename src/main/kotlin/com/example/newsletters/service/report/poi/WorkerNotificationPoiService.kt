package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.WorkerMeetingRepository
import org.springframework.stereotype.Service

@Service
class WorkerNotificationPoiService(
    private val debtorRepository: DebtorRepository,
    private val workerMeetingRepository: WorkerMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val poiTemplateService: PoiTemplateService,
) : PoiService {

    override fun generate(document: Document): ByteArray {
        val debtor = debtorRepository.findById(document.debtorId)
            .orElseThrow { IllegalStateException("Не найден должник ${document.debtorId}") }
        val meeting = workerMeetingRepository.findByDebtorId(document.debtorId).firstOrNull()
            ?: throw IllegalStateException("Не найдено собрание работников должника ${document.debtorId}")
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
            value(DEBTOR_COURT_ACT) to debtor.courtAct.orEmpty(),
            value(DEBTOR_ACT_DATE) to debtor.actDate?.format(shortDatePattern()).orEmpty(),
            value(DEBTOR_RESOLUTION_DATE) to debtor.resolutionDate?.format(shortDatePattern()).orEmpty(),
            value(DEBTOR_CASE_NUMBER) to debtor.caseNumber.orEmpty(),
            value(DEBTOR_TIN) to debtor.taxpayerIdentificationNumber.orEmpty(),
            value(DEBTOR_PSRN) to debtor.primaryStateRegistrationNumber.orEmpty(),
            value(DEBTOR_ADDRESS) to debtor.address.orEmpty(),
            value(MANAGER_TIN) to manager.taxpayerIdentificationNumber.orEmpty(),
            value(MANAGER_PIPN) to manager.personalInsurancePolicyNumber.orEmpty(),
            value(MANAGER_SD) to manager.sroData.orEmpty(),
            value(WORKER_MEETING_DATE) to meeting.date?.format(shortDatePattern()).orEmpty(),
            value(WORKER_MEETING_TIME) to meeting.time?.toString().orEmpty(),
            value(WORKER_MEETING_ADDRESS) to meeting.address.orEmpty(),
            value(WORKER_REGISTRATION_TIME_FROM) to meeting.registrationTimeFrom?.toString().orEmpty(),
            value(WORKER_REGISTRATION_TIME_TO) to meeting.registrationTimeTo?.toString().orEmpty(),
        )

        return poiTemplateService.generate(
            configuredPath = document.templateResourcePath!!,
            data = PoiReportData(values),
        )
    }
}

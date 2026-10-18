package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.WorkerMeetingRepository
import org.springframework.stereotype.Service

@Service
class Report1NPoiService(
    private val debtorRepository: DebtorRepository,
    private val workerMeetingRepository: WorkerMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val poiTemplateService: PoiTemplateService,
) : PoiService {

    override fun generate(document: Document): ByteArray {
        val debtor = debtorRepository.findById(document.debtorId)
            .orElseThrow { IllegalStateException("Не найден должник ${document.debtorId}") }
        val manager = debtor.arbitrationManagerId
            ?.let { arbitrationManagerRepository.findById(it).orElse(null) }
            ?: throw IllegalStateException("Не найден временный управляющий должника ${document.debtorId}")

        val values = mapOf(
            value(DEBTOR_COURT_ACT) to debtor.courtAct.orEmpty(),
            value(DEBTOR_CASE_NUMBER) to debtor.caseNumber.orEmpty(),
            value(DEBTOR_NAME) to debtor.name.orEmpty(),
            value(MANAGER_NAME) to manager.fullName.orEmpty(),
            value(DEBTOR_FULL_NAME) to debtor.fullName.orEmpty(),
            value(DEBTOR_TIN) to debtor.taxpayerIdentificationNumber.orEmpty(),
            value(DEBTOR_PSRN) to debtor.primaryStateRegistrationNumber.orEmpty(),
            value(DEBTOR_ADDRESS) to debtor.address.orEmpty(),
            value(MANAGER_EMAIL) to manager.email.orEmpty(),
            value(DEBTOR_ACT_DATE) to debtor.actDate?.format(shortDatePattern()).orEmpty(),
            value(MANAGER_SD) to manager.sroData.orEmpty(),
            value(MANAGER_SRO_REGISTRATION_NUMBER) to manager.sroRegistrationNumber.orEmpty(),
            value(MANAGER_INSURANCE_COMPANY_NAME) to manager.insuranceCompanyName.orEmpty(),
            value(MANAGER_INSURANCE_COMPANY_NUMBER) to manager.insuranceContractNumber.orEmpty(),
            value(MANAGER_INSURANCE_CONTRACT_TERM) to manager.insuranceContractTerm.orEmpty(),




            value(DEBTOR_NAME) to debtor.name.orEmpty(),
            value(DEBTOR_FULL_NAME) to debtor.fullName.orEmpty(),

            value(MANAGER_NAME) to manager.fullName.orEmpty(),
            value(MANAGER_EMAIL) to manager.email.orEmpty(),
            value(DEBTOR_NAME) to debtor.name.orEmpty(),
            value(DEBTOR_TIN) to debtor.taxpayerIdentificationNumber.orEmpty(),
            value(DEBTOR_SRN) to debtor.primaryStateRegistrationNumber.orEmpty(),
            value(DEBTOR_COURT_ACT) to debtor.courtAct.orEmpty(),
            value(DEBTOR_CASE_NUMBER) to debtor.caseNumber.orEmpty(),
            value(DEBTOR_ACT_DATE) to debtor.actDate?.format(shortDatePattern()).orEmpty(),
            value(DEBTOR_RESOLUTION_DATE) to debtor.resolutionDate?.format(shortDatePattern()).orEmpty(),
            value(MANAGER_TIN) to manager.taxpayerIdentificationNumber.orEmpty(),
            value(MANAGER_PIPN) to manager.personalInsurancePolicyNumber.orEmpty(),
            value(MANAGER_SD) to manager.sroData.orEmpty(),
        )

        return poiTemplateService.generate(
            configuredPath = document.templateResourcePath!!,
            data = PoiReportData(values),
        )
    }
}

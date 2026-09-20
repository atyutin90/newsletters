package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.RequestRepository
import org.springframework.stereotype.Service

private const val DEBTOR_FULL_NAME = "debtorFullName"
private const val MANAGER_CONTACTS = "managerContacts"
private const val MANAGER_POSTAL_ADDRESS = "managerPostalAddress"
private const val DOCUMENT_NUMBER = "documentNumber"
private const val REQUEST_NUMBER = "requestNumber"
private const val REQUEST_DATE = "requestDate"
private const val REQUEST_DATE_FROM = "requestDateFrom"
private const val REQUEST_DATE_TO = "requestDateTo"
private const val REQUEST_DESTINATION = "requestDestination"
private const val REQUEST_DESTINATION_DETAIL = "requestDestinationDetail"
private const val REQUEST_ADDRESS = "requestAddress"
private const val COURT_ACT = "courtAct"
private const val ACT_DATE = "actDate"
private const val RESOLUTION_DATE = "resolutionDate"
private const val CASE_NUMBER = "caseNumber"
private const val DEBTOR_TIN = "debtorTin"
private const val DEBTOR_OGRN = "debtorOgrn"
private const val DEBTOR_ADDRESS = "debtorAddress"
private const val MANAGER_TIN = "managerTin"
private const val MANAGER_SNILS = "managerSnils"
private const val MANAGER_PHONE = "managerPhone"

@Service
class RequestPoiDocumentService(
    private val debtorRepository: DebtorRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val requestRepository: RequestRepository,
    private val poiTemplateService: PoiTemplateService,
) : PoiService {

    override fun generate(document: Document): ByteArray {
        val debtor = debtorRepository.findById(document.debtorId)
            .orElseThrow { IllegalStateException("Не найден должник ${document.debtorId}") }
        val requestId = document.info?.requestId
            ?: throw IllegalStateException("Для запроса не указан идентификатор запроса")
        val request = requestRepository.findById(requestId)
            .orElseThrow { IllegalStateException("Не найден запрос $requestId") }
        val manager = debtor.arbitrationManagerId
            ?.let { arbitrationManagerRepository.findById(it).orElse(null) }
            ?: throw IllegalStateException("Не найден временный управляющий должника ${document.debtorId}")

        val values = mapOf(
            value(DEBTOR_NAME) to debtor.name.orEmpty(),
            value(DEBTOR_FULL_NAME) to debtor.fullName.orEmpty(),
            value(MANAGER_NAME) to manager.fullName.orEmpty(),
            value(MANAGER_CONTACTS) to listOf(manager.postalAddress, manager.email, manager.phone)
                .mapNotNull { it?.takeIf(String::isNotBlank) }
                .joinToString(" "),
            value(MANAGER_PHONE) to manager.phone.orEmpty(),
            value(MANAGER_POSTAL_ADDRESS) to manager.postalAddress.orEmpty(),
            value(DOCUMENT_NUMBER) to debtor.documentNumber.orEmpty(),
            value(REQUEST_NUMBER) to document.id?.toString().orEmpty(),
            value(REQUEST_DATE) to request.date?.format(shortDatePattern()).orEmpty(),
            value(REQUEST_DATE_FROM) to request.dateFrom?.format(shortDatePattern()).orEmpty(),
            value(REQUEST_DATE_TO) to request.dateTo?.format(shortDatePattern()).orEmpty(),
            value(REQUEST_DESTINATION) to request.destination?.name.orEmpty(),
            value(REQUEST_DESTINATION_DETAIL) to request.destinationDetail.orEmpty(),
            value(REQUEST_ADDRESS) to request.address.orEmpty(),
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
        )

        return poiTemplateService.generate(
            configuredPath = document.templateResourcePath!!,
            data = PoiReportData(values),
        )
    }
}

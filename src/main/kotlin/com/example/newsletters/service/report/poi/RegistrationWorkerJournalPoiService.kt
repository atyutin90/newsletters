package com.example.newsletters.service.report.poi

import com.example.newsletters.dto.report.poi.PoiReportData
import com.example.newsletters.dto.report.poi.PoiTableRow
import com.example.newsletters.entity.Document
import com.example.newsletters.repository.ArbitrationManagerRepository
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.WorkerMeetingParticipantRepository
import com.example.newsletters.repository.WorkerMeetingRepository
import org.springframework.stereotype.Service

private const val MEETING_ADDRESS_DATE_AND_TIME = "meetingAddressDateAndTime"
private const val REGISTRATION_TIME_FROM = "registrationTimeFrom"
private const val REGISTRATION_TIME_TO = "registrationTimeTo"
private const val PARTICIPANT_FULL_NAME = "participantFullName"
private const val PARTICIPANT_ADDRESS = "participantAddress"
private const val TOTAL_PARTICIPANTS = "totalParticipants"

@Service
class RegistrationWorkerJournalPoiService(
    private val debtorRepository: DebtorRepository,
    private val workerMeetingRepository: WorkerMeetingRepository,
    private val arbitrationManagerRepository: ArbitrationManagerRepository,
    private val workerMeetingParticipantRepository: WorkerMeetingParticipantRepository,
    private val poiTemplateService: PoiTemplateService,
) : PoiService {

    override fun generate(document: Document): ByteArray {
        val debtor = debtorRepository.findById(document.debtorId).orElseThrow { IllegalStateException("Не найден должник ${document.debtorId}") }
        val meeting = workerMeetingRepository.findByDebtorId(document.debtorId).firstOrNull() ?: throw IllegalStateException("Не найдено собрание работников должника ${document.debtorId}")
        val manager = debtor.arbitrationManagerId
            ?.let { arbitrationManagerRepository.findById(it).orElse(null) }
            ?: throw IllegalStateException("Не найден временный управляющий должника ${document.debtorId}")
        val participants = meeting.id
            ?.let(workerMeetingParticipantRepository::findByWorkerMeetingId)
            .orEmpty()

        val values = mapOf(
            value(DEBTOR_NAME) to debtor.name.orEmpty(),
            value(MEETING_ADDRESS_DATE_AND_TIME) to listOf(
                meeting.address.orEmpty(),
                meeting.date?.format(longDatePattern()).orEmpty(),
                meeting.time?.let { "в $it" }.orEmpty(),
            ).filter { it.isNotBlank() }.joinToString(" "),
            value(REGISTRATION_TIME_FROM) to meeting.registrationTimeFrom?.toString().orEmpty(),
            value(REGISTRATION_TIME_TO) to meeting.registrationTimeTo?.toString().orEmpty(),
            value(TOTAL_PARTICIPANTS) to participants.size.toString(),
            value(MANAGER_NAME) to manager.fullName.orEmpty(),
        )
        val participantRows = participants.mapIndexed { index, participant ->
            mapOf(
                value(ROW_NUMBER) to (index + 1).toString(),
                value(PARTICIPANT_FULL_NAME) to participant.fullName.orEmpty(),
                value(PARTICIPANT_ADDRESS) to participant.address.orEmpty(),
            )
        }

        return poiTemplateService.generate(
            configuredPath = document.templateResourcePath!!,
            data = PoiReportData(
                values,
                listOf(PoiTableRow(value(PARTICIPANT_FULL_NAME), participantRows)),
            ),
        )
    }
}

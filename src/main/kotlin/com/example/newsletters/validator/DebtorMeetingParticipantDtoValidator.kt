package com.example.newsletters.validator

import com.example.newsletters.controller.CREDITOR_ID
import com.example.newsletters.controller.TYPE
import com.example.newsletters.dto.DebtorMeetingParticipantDto
import com.example.newsletters.entity.enum.MeetingParticipantType.CREDITOR
import com.example.newsletters.entity.enum.MeetingParticipantType.Companion.meetingParticipantTypeOf
import com.example.newsletters.entity.enum.MeetingParticipantType.DEBTOR
import com.example.newsletters.entity.enum.MeetingParticipantType.PARTICIPANT
import com.example.newsletters.repository.DebtorMeetingParticipantRepository
import org.apache.commons.lang3.StringUtils.EMPTY
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import java.util.Locale.getDefault

@Component
class DebtorMeetingParticipantDtoValidator(
    private val debtorMeetingParticipantRepository: DebtorMeetingParticipantRepository,
    private val messageSource: MessageSource
) : Validator {
    override fun supports(clazz: Class<*>): Boolean =
        DebtorMeetingParticipantDto::class.java == clazz

    override fun validate(target: Any, errors: Errors) {
        (target as? DebtorMeetingParticipantDto)?.let { dto ->
            when (meetingParticipantTypeOf(dto.type)) {
                DEBTOR -> checkDebtorParticipant(dto, errors)
                CREDITOR -> checkCreditorParticipant(dto, errors)
                PARTICIPANT -> checkNewParticipant(dto, errors)
                else -> {}
            }

        }
    }

    private fun checkNewParticipant(dto: DebtorMeetingParticipantDto, errors: Errors?) {
        //Nothing
    }

    private fun checkCreditorParticipant(dto: DebtorMeetingParticipantDto, errors: Errors?) {
        if (dto.creditorId != null) {
            val debtorMeetingParticipants = debtorMeetingParticipantRepository.findByType(CREDITOR)
            val isExistParticipant = dto.id.let { id -> debtorMeetingParticipants.any { it.id == id && it.creditorId == dto.creditorId } }
            val isExistCreditor = dto.id.let { id -> debtorMeetingParticipants.any { it.creditorId == dto.creditorId && it.id != id} }
            if (debtorMeetingParticipants.isNotEmpty() && !isExistParticipant && isExistCreditor ) {
                errors?.rejectValue(
                    CREDITOR_ID,
                    EMPTY,
                    messageSource.getMessage(
                        "debtor.debtor-meeting.debtor-meeting-participant.duplicate-creditor-error-message",
                        arrayOf(),
                        getDefault()
                    )
                )
            }
        }
    }

    private fun checkDebtorParticipant(dto: DebtorMeetingParticipantDto, errors: Errors?) {
        val debtorMeetingParticipants= debtorMeetingParticipantRepository.findByType(DEBTOR)
        val isExistParticipant = dto.id?.let { id -> debtorMeetingParticipants.any { it.id == id } } ?: false
        if (debtorMeetingParticipants.isNotEmpty() && !isExistParticipant) {
            errors?.rejectValue(
                TYPE,
                EMPTY,
                messageSource.getMessage(
                    "debtor.debtor-meeting.debtor-meeting-participant.duplicate-debtor-error-message",
                    arrayOf(),
                    getDefault()
                )
            )
        }
    }
}
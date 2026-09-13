package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorMeetingParticipantDto
import com.example.newsletters.service.CreditorStorageService
import com.example.newsletters.service.DebtorMeetingParticipantStorageService
import com.example.newsletters.service.DebtorMeetingStorageService
import com.example.newsletters.service.ValueListService
import com.example.newsletters.validator.DebtorMeetingParticipantDtoValidator
import jakarta.validation.Valid
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class DebtorMeetingParticipantController(
    val debtorMeetingService: DebtorMeetingStorageService,
    val creditorService: CreditorStorageService,
    val debtorMeetingParticipantService: DebtorMeetingParticipantStorageService,
    val validator: DebtorMeetingParticipantDtoValidator,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val meetingParticipantTypes = valueListService.getValues("meetingParticipantType")

    @GetMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}/participants/new")
    fun create(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        model: Model
    ): String = run {
        formAttributes(
            model,
            DebtorMeetingParticipantDto(debtorMeetingId = debtorMeetingId),
            debtorId,
            debtorMeetingId,
            false
        )
        "debtor-meeting-participant/form"
    }

    @GetMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}/participants/{debtorMeetingParticipantId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_PARTICIPANT_ID) debtorMeetingParticipantId: Long,
        model: Model
    ): String = run {
        formAttributes(
            model,
            debtorMeetingParticipantService.getById(debtorMeetingParticipantId)!!,
            debtorId,
            debtorMeetingId,
            true
        )
        "debtor-meeting-participant/form"
    }

    @PostMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}/participants")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @Valid @ModelAttribute(DEBTOR_MEETING_PARTICIPANT) participant: DebtorMeetingParticipantDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = participant.copy(debtorMeetingId = debtorMeetingId)
        val isUpdate = data.id != null
        validator.validate(data, bindingResult)
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, debtorId, debtorMeetingId, isUpdate)
            return "debtor-meeting-participant/form"
        }

        try {
            val saved = if (isUpdate) {
                debtorMeetingParticipantService.update(data)
            } else {
                debtorMeetingParticipantService.create(data)
            }
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/debtor-meetings/${debtorMeetingId}/participants/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/debtor-meetings/${debtorMeetingId}/participants/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/debtor-meetings/${debtorMeetingId}/participants/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}/participants/{debtorMeetingParticipantId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_PARTICIPANT_ID) debtorMeetingParticipantId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        debtorMeetingParticipantService.delete(debtorMeetingParticipantId)
        messageDeleteRecord(redirectAttributes, debtorMeetingParticipantId)
        "redirect:/debtors/${debtorId}/debtor-meetings/${debtorMeetingId}"
    }

    private fun formAttributes(
        model: Model,
        participant: DebtorMeetingParticipantDto,
        debtorId: Long,
        debtorMeetingId: Long,
        isEdit: Boolean
    ) {
        model.addAttribute(MEETING_PARTICIPANT_TYPE, meetingParticipantTypes)
        model.addAttribute(DEBTOR_MEETING, debtorMeetingService.getById(debtorMeetingId))
        model.addAttribute(DEBTOR_MEETING_PARTICIPANT, participant)
        model.addAttribute(CREDITORS, creditorService.getByDebtorId(debtorId))
        model.addAttribute(IS_EDIT, isEdit)
    }
}

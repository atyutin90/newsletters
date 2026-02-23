package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorMeetingParticipantDto
import com.example.newsletters.service.*
import com.example.newsletters.validator.DebtorMeetingParticipantDtoValidator
import jakarta.validation.Valid
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.Locale.getDefault

@Controller
@RequestMapping("/debtor/{debtorId}/debtor-meeting/{debtorMeetingId}/debtor-meeting-participant")
class DebtorMeetingParticipantController(
    val debtorStorageService: DebtorStorageService,
    val debtorMeetingService: DebtorMeetingStorageService,
    val creditorService: CreditorStorageService,
    val debtorMeetingParticipantService: DebtorMeetingParticipantStorageService,
    val validator: DebtorMeetingParticipantDtoValidator,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val meetingParticipantTypes = valueListService.getValues("meetingParticipantType")

    @GetMapping("/{debtorMeetingParticipantId}/delete")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_PARTICIPANT_ID) debtorMeetingParticipantId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            debtorMeetingParticipantService.delete(debtorMeetingParticipantId)
            infoMessageDeleteRecord(redirectAttributes, debtorMeetingParticipantId)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/${debtorId}/debtor-meeting/$debtorMeetingId"
    }

    @GetMapping("/{debtorMeetingParticipantId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_PARTICIPANT_ID) debtorMeetingParticipantId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes): String = try {
        val debtorMeetingParticipant  = debtorMeetingParticipantService.getById(debtorMeetingParticipantId)

        page(debtorMeetingId, debtorId, model, debtorMeetingParticipant!!)
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${debtorId}/debtor-meeting/$debtorMeetingId"
    }

    @GetMapping("/new")
    fun add(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = try {
        page(debtorMeetingId, debtorId, model, DebtorMeetingParticipantDto(debtorMeetingId = debtorMeetingId))
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${debtorId}/debtor-meeting/${debtorMeetingId}"
    }

    @PostMapping("/save")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @Valid @ModelAttribute("debtorMeetingParticipant") request: DebtorMeetingParticipantDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes): String = run {
        validator.validate(request, bindingResult)
        if (bindingResult.hasErrors()) {
            page(debtorMeetingId, debtorId, model, request)
        } else {
            try {
                val isUpdate = request.id != null
                if (isUpdate) debtorMeetingParticipantService.update(request)
                else debtorMeetingParticipantService.create(request)
                infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
                "redirect:/debtor/${debtorId}/debtor-meeting/${debtorMeetingId}"
            } catch (e: Exception) {
                redirectAttributes.addAttribute(MESSAGE, e.message)
                "redirect:/debtor/${debtorId}/debtor-meeting/${debtorMeetingId}"
            }
        }
    }

    private fun page(
        debtorMeetingId: Long,
        debtorId: Long,
        model: Model,
        request: DebtorMeetingParticipantDto,
    ): String = run {
        val isUpdate = request.id != null
        val workerMeeting = debtorMeetingService.getById(debtorMeetingId)
        val creditors = creditorService.getByDebtorId(debtorId)
        model.addAttribute(MEETING_PARTICIPANT_TYPE, meetingParticipantTypes)
        model.addAttribute(DEBTOR_MEETING, workerMeeting)
        model.addAttribute(DEBTOR_MEETING_PARTICIPANT, request)
        model.addAttribute(CREDITORS, creditors)
        model.addAttribute(
            PAGE_TITLE,
            messageSource.getMessage(
                if (isUpdate) {
                    "debtor.debtor-meeting.debtor-meeting-participant.update"
                } else {
                    "debtor.debtor-meeting.debtor-meeting-participant.creation"
                }, arrayOf(), getDefault()
            )
        )
        "debtor-meeting-participant/form"
    }
}

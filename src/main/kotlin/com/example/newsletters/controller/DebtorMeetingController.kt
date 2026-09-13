package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.DebtorMeetingDto
import com.example.newsletters.service.DebtorMeetingParticipantStorageService
import com.example.newsletters.service.DebtorMeetingQuestionStorageService
import com.example.newsletters.service.DebtorMeetingStorageService
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.ValueListService
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
class DebtorMeetingController(
    val debtorStorageService: DebtorStorageService,
    val debtorMeetingService: DebtorMeetingStorageService,
    val debtorMeetingQuestionService: DebtorMeetingQuestionStorageService,
    val debtorMeetingParticipantService: DebtorMeetingParticipantStorageService,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val meetingParticipantTypes = valueListService.getValues("meetingParticipantType")

    @GetMapping("/debtors/{debtorId}/debtor-meetings/new")
    fun create(@PathVariable(DEBTOR_ID) debtorId: Long, model: Model): String = run {
        formAttributes(model, DebtorMeetingDto(debtorId = debtorId), debtorId, false)
        "debtor-meeting/form"
    }

    @GetMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        model: Model
    ): String = run {
        formAttributes(model, debtorMeetingService.getById(debtorMeetingId), debtorId, true)
        "debtor-meeting/form"
    }

    @PostMapping("/debtors/{debtorId}/debtor-meetings")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @Valid @ModelAttribute(DEBTOR_MEETING) debtorMeeting: DebtorMeetingDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = debtorMeeting.copy(debtorId = debtorId)
        val isUpdate = data.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, debtorId, isUpdate)
            return "debtor-meeting/form"
        }

        try {
            val saved = if (isUpdate) debtorMeetingService.update(data) else debtorMeetingService.create(data)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/debtor-meetings/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/debtor-meetings/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/debtor-meetings/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        debtorMeetingService.delete(debtorMeetingId)
        messageDeleteRecord(redirectAttributes, debtorMeetingId)
        "redirect:/debtors/${debtorId}"
    }

    private fun formAttributes(model: Model, debtorMeeting: DebtorMeetingDto, debtorId: Long, isEdit: Boolean) {
        val debtor: DebtorDto = debtorStorageService.getById(debtorId)
        model.addAttribute(DEBTOR_MEETING, debtorMeeting)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(IS_EDIT, isEdit)
        model.addAttribute(MEETING_PARTICIPANT_TYPE, meetingParticipantTypes)

        if (!isEdit || debtorMeeting.id == null) return

        model.addAttribute(
            DEBTOR_MEETING_QUESTIONS,
            debtorMeetingQuestionService.getByWorkerMeetingId(debtorMeeting.id)
        )
        model.addAttribute(
            DEBTOR_MEETING_PARTICIPANTS,
            debtorMeetingParticipantService.getByDebtorMeetingId(debtorMeeting.id)
        )
    }
}

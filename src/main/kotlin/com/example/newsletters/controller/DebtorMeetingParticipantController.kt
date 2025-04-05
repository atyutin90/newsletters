package com.example.newsletters.controller

import com.example.newsletters.dto.*
import com.example.newsletters.service.*
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/debtor/{debtorId}/debtor-meeting/{debtorMeetingId}/debtor-meeting-participant")
class DebtorMeetingParticipantController(
    val debtorStorageService: DebtorStorageService,
    val debtorMeetingService: DebtorMeetingStorageService,
    val debtorMeetingParticipantService: DebtorMeetingParticipantStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/{debtorMeetingParticipantId}/delete")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_PARTICIPANT_ID) debtorMeetingParticipantId: Long,
        model: Model,
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
        val debtorMeeting: DebtorMeetingDto? = debtorMeetingService.getById(debtorMeetingId)
        model.addAttribute(DEBTOR_MEETING_PARTICIPANT, debtorMeetingParticipant)
        model.addAttribute(DEBTOR_MEETING, debtorMeeting)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.debtor-meeting.debtor-meeting-participant.update", arrayOf(debtorMeetingParticipantId), Locale.getDefault()))
        "debtor-meeting-participant/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${debtorId}/debtor-meeting/$debtorMeetingId"
    }

    @GetMapping("/new")
    fun add(@PathVariable(DEBTOR_ID) debtorId: Long, @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long, model: Model, redirectAttributes: RedirectAttributes): String = try {
        val workerMeeting = debtorMeetingService.getById(debtorMeetingId)
        model.addAttribute(DEBTOR_MEETING_PARTICIPANT, DebtorMeetingParticipantDto(debtorMeetingId = debtorMeetingId))
        model.addAttribute(DEBTOR_MEETING, workerMeeting)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.debtor-meeting.debtor-meeting-participant.creation", arrayOf(), Locale.getDefault()))
        "debtor-meeting-participant/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${debtorId}/debtor-meeting/${debtorMeetingId}"
    }

    @PostMapping("/save")
    fun save(request: DebtorMeetingParticipantDto, model: Model, redirectAttributes: RedirectAttributes): String = run {
        val debtorMeeting: DebtorMeetingDto = request.debtorMeetingId.let { debtorMeetingService.getById(it) }
        try {
            val isUpdate = request.id != null
            if (isUpdate) debtorMeetingParticipantService.update(request)
            else debtorMeetingParticipantService.create(request)
            infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtor/${debtorMeeting.debtorId}/debtor-meeting/${debtorMeeting.id}"
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
            "redirect:/debtor/${debtorMeeting.debtorId}/debtor-meeting/${debtorMeeting.id}"
        }
    }
}

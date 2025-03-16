package com.example.newsletters.controller

import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.WorkerMeetingDto
import com.example.newsletters.dto.WorkerMeetingParticipantDto
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.WorkerMeetingParticipantStorageService
import com.example.newsletters.service.WorkerMeetingStorageService
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
@RequestMapping("/debtor/{debtorId}/worker-meeting/{workerMeetingId}/worker-meeting-participant")
class WorkerMeetingParticipantController(
    val debtorStorageService: DebtorStorageService,
    val workerMeetingService: WorkerMeetingStorageService,
    val workerMeetingParticipantService: WorkerMeetingParticipantStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/{workerMeetingParticipantId}/delete")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        @PathVariable(WORKER_MEETING_PARTICIPANT_ID) workerMeetingParticipantId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            workerMeetingParticipantService.delete(workerMeetingParticipantId)
            infoMessageDeleteRecord(redirectAttributes, workerMeetingParticipantId)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/${debtorId}/worker-meeting/$workerMeetingId"
    }

    @GetMapping("/{workerMeetingParticipantId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        @PathVariable(WORKER_MEETING_PARTICIPANT_ID) workerMeetingParticipantId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes): String = try {
        val workerMeetingParticipant  = workerMeetingParticipantService.getById(workerMeetingParticipantId)
        val workerMeeting: WorkerMeetingDto? = workerMeetingService.getById(workerMeetingId)
        model.addAttribute(WORKER_MEETING_PARTICIPANT, workerMeetingParticipant)
        model.addAttribute(WORKER_MEETING, workerMeeting)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.worker-meeting.worker-meeting-participant.update", arrayOf(workerMeetingParticipantId), Locale.getDefault()))
        "worker-meeting-participant/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${debtorId}/worker-meeting/$workerMeetingId"
    }

    @GetMapping("/new")
    fun add(@PathVariable(DEBTOR_ID) debtorId: Long, @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long, model: Model, redirectAttributes: RedirectAttributes): String = try {
        val workerMeeting = workerMeetingService.getById(workerMeetingId)
        model.addAttribute(WORKER_MEETING_PARTICIPANT,  WorkerMeetingParticipantDto(workerMeetingId = workerMeetingId))
        model.addAttribute(WORKER_MEETING, workerMeeting)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.worker-meeting.worker-meeting-participant.creation", arrayOf(), Locale.getDefault()))
        "worker-meeting-participant/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${debtorId}/worker-meeting/${workerMeetingId}"
    }

    @PostMapping("/save")
    fun save(request: WorkerMeetingParticipantDto, model: Model, redirectAttributes: RedirectAttributes): String = run {
        val workerMeeting: WorkerMeetingDto = request.workerMeetingId.let { workerMeetingService.getById(it) }
        try {
            val isUpdate = request.id != null
            if (isUpdate) workerMeetingParticipantService.update(request)
            else workerMeetingParticipantService.create(request)
            infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtor/${workerMeeting.debtorId}/worker-meeting/${workerMeeting.id}"
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
            "redirect:/debtor/${workerMeeting.debtorId}/worker-meeting/${workerMeeting.id}"
        }
    }
}

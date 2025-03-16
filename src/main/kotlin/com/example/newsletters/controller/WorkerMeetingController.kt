package com.example.newsletters.controller

import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.RequestDestinationDto
import com.example.newsletters.dto.WorkerMeetingDto
import com.example.newsletters.dto.WorkerMeetingParticipantDto
import com.example.newsletters.entity.WorkerMeetingParticipant
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
@RequestMapping("/debtor/{id}/worker-meeting")
class WorkerMeetingController(
    val debtorStorageService: DebtorStorageService,
    val workerMeetingService: WorkerMeetingStorageService,
    val workerMeetingParticipantService: WorkerMeetingParticipantStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/{workerMeetingId}/delete")
    fun delete(
        @PathVariable(ID) id: Long,
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            workerMeetingService.delete(workerMeetingId)
             infoMessageDeleteRecord(redirectAttributes, workerMeetingId)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/${id}"
    }

    @GetMapping("/{workerMeetingId}")
    fun edit(
        @PathVariable(ID) id: Long,
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes): String = try {
        val workerMeeting: WorkerMeetingDto = workerMeetingService.getById(workerMeetingId)
        val requestDestinations: List<WorkerMeetingParticipantDto> = workerMeetingParticipantService.getByWorkerMeetingId(workerMeetingId)
        val debtor: DebtorDto = debtorStorageService.getById(id)
        model.addAttribute(WORKER_MEETING, workerMeeting)
        model.addAttribute(WORKER_MEETING_PARTICIPANTS, requestDestinations)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.worker-meeting.update", arrayOf(workerMeetingId), Locale.getDefault()))
        "worker-meeting/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${id}"
    }

    @GetMapping("/new")
    fun add(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String = try {
        val debtor: DebtorDto = debtorStorageService.getById(id)
        model.addAttribute(WORKER_MEETING,  WorkerMeetingDto(debtorId = id))
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.worker-meeting.creation", arrayOf(), Locale.getDefault()))
        "worker-meeting/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/{id}"
    }

    @PostMapping("/save")
    fun save(request: WorkerMeetingDto, model: Model, redirectAttributes: RedirectAttributes): String = try {
        val isUpdate = request.id != null
        if (isUpdate) workerMeetingService.update(request)
        else workerMeetingService.create(request)
        infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        "redirect:/debtor/${request.debtorId}"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${request.debtorId}"
    }
}

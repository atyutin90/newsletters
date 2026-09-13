package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.WorkerMeetingDto
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.WorkerMeetingParticipantStorageService
import com.example.newsletters.service.WorkerMeetingStorageService
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
class WorkerMeetingController(
    val debtorStorageService: DebtorStorageService,
    val workerMeetingService: WorkerMeetingStorageService,
    val workerMeetingParticipantService: WorkerMeetingParticipantStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/debtors/{debtorId}/worker-meetings/new")
    fun create(@PathVariable(DEBTOR_ID) debtorId: Long, model: Model): String = run {
        formAttributes(model, WorkerMeetingDto(debtorId = debtorId), debtorId, false)
        "worker-meeting/form"
    }

    @GetMapping("/debtors/{debtorId}/worker-meetings/{workerMeetingId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        model: Model
    ): String = run {
        formAttributes(model, workerMeetingService.getById(workerMeetingId), debtorId, true)
        "worker-meeting/form"
    }

    @PostMapping("/debtors/{debtorId}/worker-meetings")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @Valid @ModelAttribute(WORKER_MEETING) workerMeeting: WorkerMeetingDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = workerMeeting.copy(debtorId = debtorId)
        val isUpdate = data.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, debtorId, isUpdate)
            return "worker-meeting/form"
        }

        try {
            val saved = if (isUpdate) workerMeetingService.update(data) else workerMeetingService.create(data)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/worker-meetings/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/worker-meetings/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/worker-meetings/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/worker-meetings/{workerMeetingId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        workerMeetingService.delete(workerMeetingId)
        messageDeleteRecord(redirectAttributes, workerMeetingId)
        "redirect:/debtors/${debtorId}"
    }

    private fun formAttributes(model: Model, workerMeeting: WorkerMeetingDto, debtorId: Long, isEdit: Boolean) {
        val debtor: DebtorDto = debtorStorageService.getById(debtorId)
        model.addAttribute(WORKER_MEETING, workerMeeting)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(IS_EDIT, isEdit)

        if (!isEdit || workerMeeting.id == null) return

        model.addAttribute(
            WORKER_MEETING_PARTICIPANTS,
            workerMeetingParticipantService.getByWorkerMeetingId(workerMeeting.id)
        )
    }
}

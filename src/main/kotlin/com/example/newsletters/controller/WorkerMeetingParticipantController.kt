package com.example.newsletters.controller

import com.example.newsletters.dto.model.WorkerMeetingParticipantDto
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
class WorkerMeetingParticipantController(
    val workerMeetingService: WorkerMeetingStorageService,
    val workerMeetingParticipantService: WorkerMeetingParticipantStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/debtors/{debtorId}/worker-meetings/{workerMeetingId}/participants/new")
    fun create(
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        model: Model
    ): String = run {
        formAttributes(model, WorkerMeetingParticipantDto(workerMeetingId = workerMeetingId), workerMeetingId, false)
        "worker-meeting-participant/form"
    }

    @GetMapping("/debtors/{debtorId}/worker-meetings/{workerMeetingId}/participants/{workerMeetingParticipantId}")
    fun edit(
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        @PathVariable(WORKER_MEETING_PARTICIPANT_ID) workerMeetingParticipantId: Long,
        model: Model
    ): String = run {
        formAttributes(
            model,
            workerMeetingParticipantService.getById(workerMeetingParticipantId),
            workerMeetingId,
            true
        )
        "worker-meeting-participant/form"
    }

    @PostMapping("/debtors/{debtorId}/worker-meetings/{workerMeetingId}/participants")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        @Valid @ModelAttribute(WORKER_MEETING_PARTICIPANT) participant: WorkerMeetingParticipantDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = participant.copy(workerMeetingId = workerMeetingId)
        val isUpdate = data.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, workerMeetingId, isUpdate)
            return "worker-meeting-participant/form"
        }

        try {
            val saved = if (isUpdate) {
                workerMeetingParticipantService.update(data)
            } else {
                workerMeetingParticipantService.create(data)
            }
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/worker-meetings/${workerMeetingId}/participants/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/worker-meetings/${workerMeetingId}/participants/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/worker-meetings/${workerMeetingId}/participants/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/worker-meetings/{workerMeetingId}/participants/{workerMeetingParticipantId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(WORKER_MEETING_ID) workerMeetingId: Long,
        @PathVariable(WORKER_MEETING_PARTICIPANT_ID) workerMeetingParticipantId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        workerMeetingParticipantService.delete(workerMeetingParticipantId)
        messageDeleteRecord(redirectAttributes, workerMeetingParticipantId)
        "redirect:/debtors/${debtorId}/worker-meetings/${workerMeetingId}"
    }

    private fun formAttributes(
        model: Model,
        participant: WorkerMeetingParticipantDto,
        workerMeetingId: Long,
        isEdit: Boolean
    ) {
        model.addAttribute(WORKER_MEETING_PARTICIPANT, participant)
        model.addAttribute(WORKER_MEETING, workerMeetingService.getById(workerMeetingId))
        model.addAttribute(IS_EDIT, isEdit)
    }
}

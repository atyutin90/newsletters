package com.example.newsletters.controller

import com.example.newsletters.dto.*
import com.example.newsletters.service.DebtorMeetingParticipantStorageService
import com.example.newsletters.service.DebtorMeetingQuestionStorageService
import com.example.newsletters.service.DebtorMeetingStorageService
import com.example.newsletters.service.DebtorStorageService
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
@RequestMapping("/debtor/{id}/debtor-meeting")
class DebtorMeetingController(
    val debtorStorageService: DebtorStorageService,
    val debtorMeetingService: DebtorMeetingStorageService,
    val debtorMeetingQuestionService: DebtorMeetingQuestionStorageService,
    val debtorMeetingParticipantService: DebtorMeetingParticipantStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/new")
    fun add(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String = try {
        val debtor: DebtorDto = debtorStorageService.getById(id)
        model.addAttribute(DEBTOR_MEETING,  DebtorMeetingDto(debtorId = id))
        model.addAttribute(DEBTOR,  debtor)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.debtor-meeting.creation", arrayOf(), Locale.getDefault()))
        "debtor-meeting/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/{id}"
    }

    @PostMapping("/save")
    fun save(request: DebtorMeetingDto, redirectAttributes: RedirectAttributes): String = try {
        val isUpdate = request.id != null
        if (isUpdate) debtorMeetingService.update(request)
        else debtorMeetingService.create(request)
       infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        "redirect:/debtor/${request.debtorId}"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${request.debtorId}"
    }

    @GetMapping("/{debtorMeetingId}")
    fun edit(
        @PathVariable(ID) id: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = try {
        val debtorMeeting: DebtorMeetingDto = debtorMeetingService.getById(debtorMeetingId)
        val questions: List<DebtorMeetingQuestionDto> = debtorMeetingQuestionService.getByWorkerMeetingId(debtorMeetingId)
        val participants: List<DebtorMeetingParticipantDto> = debtorMeetingParticipantService.getByDebtorMeetingId(debtorMeetingId)
        val debtor: DebtorDto = debtorStorageService.getById(id)
        model.addAttribute(DEBTOR_MEETING, debtorMeeting)
        model.addAttribute(DEBTOR_MEETING_QUESTIONS, questions)
        model.addAttribute(DEBTOR_MEETING_PARTICIPANTS, participants)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-debtor-meeting", arrayOf(debtorMeetingId), Locale.getDefault()))
        "debtor-meeting/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${id}"
    }

    @GetMapping("/{debtorMeetingId}/delete")
    fun delete(
        @PathVariable(ID) id: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            debtorMeetingService.delete(debtorMeetingId)
            infoMessageDeleteRecord(redirectAttributes, debtorMeetingId)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/${id}"
    }
}
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
@RequestMapping("/debtor/{debtorId}/debtor-meeting/{debtorMeetingId}/debtor-meeting-question")
class DebtorMeetingQuestionController(
    val debtorStorageService: DebtorStorageService,
    val debtorMeetingService: DebtorMeetingStorageService,
    val debtorMeetingQuestionService: DebtorMeetingQuestionStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/{debtorMeetingQuestionId}/delete")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_QUESTION_ID) debtorMeetingQuestionId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            debtorMeetingQuestionService.delete(debtorMeetingQuestionId)
            infoMessageDeleteRecord(redirectAttributes, debtorMeetingQuestionId)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/${debtorId}/debtor-meeting/$debtorMeetingId"
    }

    @GetMapping("/{debtorMeetingQuestionId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_QUESTION_ID) debtorMeetingQuestionId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = try {
        val debtorMeetingQuestion = debtorMeetingQuestionService.getById(debtorMeetingQuestionId)
        val debtorMeeting: DebtorMeetingDto? = debtorMeetingService.getById(debtorMeetingId)
        model.addAttribute(DEBTOR_MEETING_QUESTION, debtorMeetingQuestion)
        model.addAttribute(DEBTOR_MEETING, debtorMeeting)
        model.addAttribute(
            PAGE_TITLE,
            messageSource.getMessage(
                "debtor.debtor-meeting.debtor-meeting-question.update",
                arrayOf(debtorMeetingQuestion.id),
                Locale.getDefault()
            )
        )
        "debtor-meeting-question/form"
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
        val debtorMeeting = debtorMeetingService.getById(debtorMeetingId)
        model.addAttribute(DEBTOR_MEETING_QUESTION, DebtorMeetingQuestionDto(debtorMeetingId = debtorMeetingId))
        model.addAttribute(DEBTOR_MEETING, debtorMeeting)
        model.addAttribute(
            PAGE_TITLE,
            messageSource.getMessage(
                "debtor.debtor-meeting.debtor-meeting-question.creation",
                arrayOf(),
                Locale.getDefault()
            )
        )
        "debtor-meeting-question/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${debtorId}/debtor-meeting/${debtorMeetingId}"
    }

    @PostMapping("/save")
    fun save(
        request: DebtorMeetingQuestionDto,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val debtorMeeting = request.debtorMeetingId.let { debtorMeetingService.getById(it) }
        try {
            val isUpdate = request.id != null
            if (isUpdate) debtorMeetingQuestionService.update(request)
            else debtorMeetingQuestionService.create(request)
            infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtor/${debtorMeeting.debtorId}/debtor-meeting/${debtorMeeting.id}"
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
            "redirect:/debtor/${debtorMeeting.debtorId}/debtor-meeting/${debtorMeeting.id}"
        }
    }
}

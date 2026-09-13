package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorMeetingQuestionDto
import com.example.newsletters.service.DebtorMeetingQuestionStorageService
import com.example.newsletters.service.DebtorMeetingStorageService
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
class DebtorMeetingQuestionController(
    val debtorMeetingService: DebtorMeetingStorageService,
    val debtorMeetingQuestionService: DebtorMeetingQuestionStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}/questions/new")
    fun create(
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        model: Model
    ): String = run {
        formAttributes(model, DebtorMeetingQuestionDto(debtorMeetingId = debtorMeetingId), debtorMeetingId, false)
        "debtor-meeting-question/form"
    }

    @GetMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}/questions/{debtorMeetingQuestionId}")
    fun edit(
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_QUESTION_ID) debtorMeetingQuestionId: Long,
        model: Model
    ): String = run {
        formAttributes(model, debtorMeetingQuestionService.getById(debtorMeetingQuestionId), debtorMeetingId, true)
        "debtor-meeting-question/form"
    }

    @PostMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}/questions")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @Valid @ModelAttribute(DEBTOR_MEETING_QUESTION) question: DebtorMeetingQuestionDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = question.copy(debtorMeetingId = debtorMeetingId)
        val isUpdate = data.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, debtorMeetingId, isUpdate)
            return "debtor-meeting-question/form"
        }

        try {
            val saved = if (isUpdate) {
                debtorMeetingQuestionService.update(data)
            } else {
                debtorMeetingQuestionService.create(data)
            }
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/debtor-meetings/${debtorMeetingId}/questions/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/debtor-meetings/${debtorMeetingId}/questions/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/debtor-meetings/${debtorMeetingId}/questions/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/debtor-meetings/{debtorMeetingId}/questions/{debtorMeetingQuestionId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(DEBTOR_MEETING_ID) debtorMeetingId: Long,
        @PathVariable(DEBTOR_MEETING_QUESTION_ID) debtorMeetingQuestionId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        debtorMeetingQuestionService.delete(debtorMeetingQuestionId)
        messageDeleteRecord(redirectAttributes, debtorMeetingQuestionId)
        "redirect:/debtors/${debtorId}/debtor-meetings/${debtorMeetingId}"
    }

    private fun formAttributes(
        model: Model,
        question: DebtorMeetingQuestionDto,
        debtorMeetingId: Long,
        isEdit: Boolean
    ) {
        model.addAttribute(DEBTOR_MEETING_QUESTION, question)
        model.addAttribute(DEBTOR_MEETING, debtorMeetingService.getById(debtorMeetingId))
        model.addAttribute(IS_EDIT, isEdit)
    }
}

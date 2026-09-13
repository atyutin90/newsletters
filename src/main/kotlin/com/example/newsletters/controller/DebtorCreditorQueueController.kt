package com.example.newsletters.controller

import com.example.newsletters.dto.model.CreditorDto
import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.QueueDto
import com.example.newsletters.service.CreditorStorageService
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.QueueStorageService
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
class DebtorCreditorQueueController(
    val creditorStorageService: CreditorStorageService,
    val queueStorageService: QueueStorageService,
    val debtorStorageService: DebtorStorageService,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val queueTypes = valueListService.getValues("queueType")

    @GetMapping("/debtors/{debtorId}/creditors/{creditorId}/queues/new")
    fun create(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        model: Model
    ): String = run {
        formAttributes(model, QueueDto(creditorId = creditorId), debtorId, creditorId, false)
        "debtor-creditor-queue/form"
    }

    @GetMapping("/debtors/{debtorId}/creditors/{creditorId}/queues/{queueId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        @PathVariable(QUEUE_ID) queueId: Long,
        model: Model
    ): String = run {
        formAttributes(model, queueStorageService.getById(queueId), debtorId, creditorId, true)
        "debtor-creditor-queue/form"
    }

    @PostMapping("/debtors/{debtorId}/creditors/{creditorId}/queues")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        @Valid @ModelAttribute(QUEUE) queue: QueueDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = queue.copy(creditorId = creditorId)
        val isUpdate = data.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, debtorId, creditorId, isUpdate)
            return "debtor-creditor-queue/form"
        }

        try {
            val saved = if (isUpdate) queueStorageService.update(data) else queueStorageService.create(data)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/creditors/${creditorId}/queues/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/creditors/${creditorId}/queues/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/creditors/${creditorId}/queues/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/creditors/{creditorId}/queues/{queueId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        @PathVariable(QUEUE_ID) queueId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        queueStorageService.delete(queueId)
        messageDeleteRecord(redirectAttributes, queueId)
        "redirect:/debtors/${debtorId}/creditors/${creditorId}"
    }

    private fun formAttributes(
        model: Model,
        queue: QueueDto,
        debtorId: Long,
        creditorId: Long,
        isEdit: Boolean
    ) {
        val debtor: DebtorDto = debtorStorageService.getById(debtorId)
        val creditor: CreditorDto = creditorStorageService.getById(creditorId)
        model.addAttribute(QUEUE, queue)
        model.addAttribute(QUEUE_TYPES, queueTypes)
        model.addAttribute(CREDITOR, creditor)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(IS_EDIT, isEdit)
    }
}

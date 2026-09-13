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
class DebtorCreditorController(
    val debtorStorageService: DebtorStorageService,
    val creditorStorageService: CreditorStorageService,
    val queueStorageService: QueueStorageService,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val clientTypes = valueListService.getValues("clientType")
    val queueTypes = valueListService.getValues("queueType")

    @GetMapping("/debtors/{debtorId}/creditors/new")
    fun create(@PathVariable(DEBTOR_ID) debtorId: Long, model: Model): String = run {
        formAttributes(model, CreditorDto(debtorId = debtorId), debtorId, false)
        "debtor-creditor/form"
    }

    @GetMapping("/debtors/{debtorId}/creditors/{creditorId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        model: Model
    ): String = run {
        formAttributes(model, creditorStorageService.getById(creditorId), debtorId, true)
        "debtor-creditor/form"
    }

    @PostMapping("/debtors/{debtorId}/creditors")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @Valid @ModelAttribute(CREDITOR) creditor: CreditorDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = creditor.copy(debtorId = debtorId)
        val isUpdate = data.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, debtorId, isUpdate)
            return "debtor-creditor/form"
        }

        try {
            val saved = if (isUpdate) creditorStorageService.update(data) else creditorStorageService.create(data)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/creditors/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/creditors/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/creditors/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/creditors/{creditorId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        creditorStorageService.delete(creditorId)
        messageDeleteRecord(redirectAttributes, creditorId)
        "redirect:/debtors/${debtorId}"
    }

    private fun formAttributes(model: Model, creditor: CreditorDto, debtorId: Long, isEdit: Boolean) {
        val debtor: DebtorDto = debtorStorageService.getById(debtorId)
        model.addAttribute(CREDITOR, creditor)
        model.addAttribute(CLIENT_TYPES, clientTypes)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(IS_EDIT, isEdit)

        if (!isEdit || creditor.id == null) return

        model.addAttribute(QUEUE, QueueDto(creditorId = creditor.id))
        model.addAttribute(QUEUES, queueStorageService.getByCreditorId(creditor.id))
        model.addAttribute(QUEUE_TYPES, queueTypes)
    }
}

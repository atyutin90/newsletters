package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.QueueDto
import com.example.newsletters.service.CreditorStorageService
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.QueueStorageService
import com.example.newsletters.service.ValueListService
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/debtor/{debtorId}/creditor/{creditorId}/queue")
class QueueController(
    val creditorStorageService: CreditorStorageService,
    val queueStorageService: QueueStorageService,
    val debtorStorageService: DebtorStorageService,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val queueTypes = valueListService.getValues("queueType")

    @GetMapping("/{queueId}/delete")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        @PathVariable(QUEUE_ID) queueId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            queueStorageService.delete(queueId)
            infoMessageDeleteRecord(redirectAttributes, queueId)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/${debtorId}/creditor/${creditorId}"
    }

    @GetMapping("/new")
    fun add(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            val creditor = creditorStorageService.getById(creditorId)
            val debtor: DebtorDto = debtorStorageService.getById(debtorId)
            model.addAttribute(QUEUE, QueueDto(creditorId = creditorId))
            model.addAttribute(QUEUE_TYPES, queueTypes)
            model.addAttribute(CREDITOR, creditor)
            model.addAttribute(DEBTOR, debtor)
            model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.creditor.queue.creation", arrayOf(), Locale.getDefault()))
            "queue/form"
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
            "redirect:/debtor/${debtorId}/creditor/${creditorId}"
        }
    }

    @PostMapping("/save")
    fun save(
        request: QueueDto,
        model: Model,
        redirectAttributes: RedirectAttributes,
        result: BindingResult
    ): String = run {
        val creditor = request.creditorId?.let { creditorStorageService.getById(it) }
        try {
            val isUpdate = request.id != null
            if (isUpdate) queueStorageService.update(request)
            else queueStorageService.create(request)
            infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtor/${creditor?.debtorId}/creditor/${request.creditorId}"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.message)
            "redirect:/debtor/${creditor?.debtorId}/creditor/${request.creditorId}"
        }
    }

    @GetMapping("/{queueId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        @PathVariable(QUEUE_ID) queueId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes): String = try {
                val queueDto = queueStorageService.getById(queueId)
                val creditor = creditorStorageService.getById(creditorId)
                val debtor: DebtorDto = debtorStorageService.getById(debtorId)
                model.addAttribute(QUEUE, queueDto)
                model.addAttribute(QUEUE_TYPES, queueTypes)
                model.addAttribute(CREDITOR, creditor)
                model.addAttribute(DEBTOR, debtor)
                model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-queue", arrayOf(queueId), Locale.getDefault()))
                "queue/form"
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
            "redirect:/debtor/${debtorId}/creditor/${creditorId}"
        }
}

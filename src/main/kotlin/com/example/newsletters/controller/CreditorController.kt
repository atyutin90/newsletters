package com.example.newsletters.controller

import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.PublicationDto
import com.example.newsletters.dto.QueueDto
import com.example.newsletters.entity.enum.ClientType
import com.example.newsletters.service.CreditorStorageService
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.PublicationStorageService
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
@RequestMapping("/debtor/{id}/creditor")
class CreditorController(
    val debtorStorageService: DebtorStorageService,
    val creditorStorageService: CreditorStorageService,
    val queueStorageService: QueueStorageService,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val clientTypes = valueListService.getValues("clientType")
    val queueTypes = valueListService.getValues("queueType")

    @GetMapping("/{creditorId}/delete")
    fun delete(
        @PathVariable(ID) id: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            creditorStorageService.delete(creditorId)
            infoMessageDeleteRecord(redirectAttributes, creditorId)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/${id}"
    }

    @GetMapping("/new")
    fun add(
        @PathVariable(ID) id: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = try {
        val debtor: DebtorDto = debtorStorageService.getById(id)
        model.addAttribute(CREDITOR,  CreditorDto(debtorId = id))
        model.addAttribute(CLIENT_TYPES, clientTypes)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.creditor.creation", arrayOf(), Locale.getDefault()))
        "creditor/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/{id}"
    }

    @PostMapping("/save")
    fun save(request: CreditorDto, model: Model, redirectAttributes: RedirectAttributes, result: BindingResult): String = try {
        val isUpdate = request.id != null
        if (isUpdate) creditorStorageService.update(request)
        else creditorStorageService.create(request)
        infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        "redirect:/debtor/${request.debtorId}"
    } catch (e: Exception) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.message)
        "redirect:/debtor/${request.debtorId}"
    }

    @GetMapping("/{creditorId}")
    fun edit(
        @PathVariable(ID) id: Long,
        @PathVariable(CREDITOR_ID) creditorId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes): String = try {
        val creditorDto: CreditorDto = creditorStorageService.getById(creditorId)
        val debtor: DebtorDto = debtorStorageService.getById(id)
        val queues = queueStorageService.getByCreditorId(creditorId)
        model.addAttribute(CREDITOR, creditorDto)
        model.addAttribute(CLIENT_TYPES, clientTypes)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(QUEUE, QueueDto(creditorId = id))
        model.addAttribute(QUEUES, queues)
        model.addAttribute(QUEUE_TYPES, queueTypes)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-creditor", arrayOf(creditorId), Locale.getDefault()))
        "creditor/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${id}"
    }
}

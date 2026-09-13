package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.RequestDestinationDto
import com.example.newsletters.dto.model.RequestDto
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.RequestDestinationService
import com.example.newsletters.service.RequestStorageService
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
class DebtorRequestController(
    val debtorStorageService: DebtorStorageService,
    val requestStorageService: RequestStorageService,
    val requestDestinationService: RequestDestinationService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/debtors/{debtorId}/requests/new")
    fun create(@PathVariable(DEBTOR_ID) debtorId: Long, model: Model): String = run {
        formAttributes(model, RequestDto(debtorId = debtorId), debtorId, false)
        "debtor-request/form"
    }

    @GetMapping("/debtors/{debtorId}/requests/{requestId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(REQUEST_ID) requestId: Long,
        model: Model
    ): String = run {
        formAttributes(model, requestStorageService.getById(requestId), debtorId, true)
        "debtor-request/form"
    }

    @PostMapping("/debtors/{debtorId}/requests")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @Valid @ModelAttribute(REQUEST) request: RequestDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = request.copy(debtorId = debtorId)
        val isUpdate = data.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, debtorId, isUpdate)
            return "debtor-request/form"
        }

        try {
            val saved = if (isUpdate) requestStorageService.update(data) else requestStorageService.create(data)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/requests/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/requests/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/requests/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/requests/{requestId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(REQUEST_ID) requestId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        requestStorageService.delete(requestId)
        messageDeleteRecord(redirectAttributes, requestId)
        "redirect:/debtors/${debtorId}"
    }

    private fun formAttributes(model: Model, request: RequestDto, debtorId: Long, isEdit: Boolean) {
        val debtor: DebtorDto = debtorStorageService.getById(debtorId)
        val requestDestinations: List<RequestDestinationDto> = requestDestinationService.getAll()
        model.addAttribute(REQUEST, request)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(REQUEST_DESTINATIONS, requestDestinations)
        model.addAttribute(IS_EDIT, isEdit)
    }
}

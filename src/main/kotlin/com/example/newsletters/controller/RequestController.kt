package com.example.newsletters.controller

import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.RequestDestinationDto
import com.example.newsletters.dto.RequestDto
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.RequestDestinationService
import com.example.newsletters.service.RequestStorageService
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
@RequestMapping("/debtor/{id}/request")
class RequestController(
    val debtorStorageService: DebtorStorageService,
    val requestStorageService: RequestStorageService,
    val requestDestinationService: RequestDestinationService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/{requestId}/delete")
    fun delete(
        @PathVariable(ID) id: Long,
        @PathVariable(REQUEST_ID) requestId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            requestStorageService.delete(requestId)
            infoMessageDeleteRecord(redirectAttributes, requestId)
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
        val requestDestinations: List<RequestDestinationDto> = requestDestinationService.getAll()
        model.addAttribute(REQUEST,  RequestDto(debtorId = id))
        model.addAttribute(DEBTOR,  debtor)
        model.addAttribute(REQUEST_DESTINATIONS, requestDestinations)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-debtor", arrayOf(), Locale.getDefault()))
        "request/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/{id}"
    }

    @PostMapping("/save")
    fun save(request: RequestDto, model: Model, redirectAttributes: RedirectAttributes): String = try {
        val isUpdate = request.id != null
        if (isUpdate) requestStorageService.update(request)
        else requestStorageService.create(request)
        infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        "redirect:/debtor/${request.debtorId}"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${request.debtorId}"
    }

    @GetMapping("/{requestId}")
    fun edit(
        @PathVariable(ID) id: Long,
        @PathVariable(REQUEST_ID) requestId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes): String = try {
        val request: RequestDto = requestStorageService.getById(requestId)
        val debtor: DebtorDto = debtorStorageService.getById(id)
        val requestDestinations: List<RequestDestinationDto> = requestDestinationService.getAll()
        model.addAttribute(REQUEST, request)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(REQUEST_DESTINATIONS, requestDestinations)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-request", arrayOf(requestId), Locale.getDefault()))
        "request/form"
    } catch (e: Exception) {
        redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        "redirect:/debtor/${id}"
    }
}

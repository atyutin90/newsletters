package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.PublicationDto
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.PublicationStorageService
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
@RequestMapping("/debtor/{id}/publication")
class PublicationController(
    val debtorStorageService: DebtorStorageService,
    val publicationStorageService: PublicationStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/{publicationId}/delete")
    fun delete(
        @PathVariable(ID) id: Long,
        @PathVariable(PUBLICATION_ID) publicationId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            publicationStorageService.delete(publicationId)
            infoMessageDeleteRecord(redirectAttributes, publicationId)
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
        model.addAttribute(PUBLICATION,  PublicationDto(debtorId = id))
        model.addAttribute(DEBTOR,  debtor)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("debtor.publication.creation", arrayOf(), Locale.getDefault()))
        "publication/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/{id}"
    }

    @PostMapping("/save")
    fun save(request: PublicationDto, model: Model, redirectAttributes: RedirectAttributes, result: BindingResult): String = try {
        val isUpdate = request.id != null
        if (isUpdate) publicationStorageService.update(request)
        else publicationStorageService.create(request)
        infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        "redirect:/debtor/${request.debtorId}"
    } catch (e: Exception) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.message)
        "redirect:/debtor/${request.debtorId}"
    }

    @GetMapping("/{publicationId}")
    fun edit(
        @PathVariable(ID) id: Long,
        @PathVariable(PUBLICATION_ID) publicationId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes): String = try {
        val publication: PublicationDto = publicationStorageService.getById(publicationId)
        val debtor: DebtorDto = debtorStorageService.getById(id)
        model.addAttribute(PUBLICATION, publication)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-publication", arrayOf(publicationId), Locale.getDefault()))
        "publication/form"
    } catch (e: Exception) {
        redirectAttributes.addAttribute(MESSAGE, e.message)
        "redirect:/debtor/${id}"
    }
}
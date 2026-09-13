package com.example.newsletters.controller

import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.PublicationDto
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.PublicationStorageService
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
class DebtorPublicationController(
    val debtorStorageService: DebtorStorageService,
    val publicationStorageService: PublicationStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/debtors/{debtorId}/publications/new")
    fun create(@PathVariable(DEBTOR_ID) debtorId: Long, model: Model): String = run {
        formAttributes(model, PublicationDto(debtorId = debtorId), debtorId, false)
        "debtor-publication/form"
    }

    @GetMapping("/debtors/{debtorId}/publications/{publicationId}")
    fun edit(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(PUBLICATION_ID) publicationId: Long,
        model: Model
    ): String = run {
        formAttributes(model, publicationStorageService.getById(publicationId), debtorId, true)
        "debtor-publication/form"
    }

    @PostMapping("/debtors/{debtorId}/publications")
    fun save(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @Valid @ModelAttribute(PUBLICATION) publication: PublicationDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val data = publication.copy(debtorId = debtorId)
        val isUpdate = data.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, data, debtorId, isUpdate)
            return "debtor-publication/form"
        }

        try {
            val saved = if (isUpdate) publicationStorageService.update(data) else publicationStorageService.create(data)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtorId}/publications/${saved.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) {
                "redirect:/debtors/${debtorId}/publications/${data.id}"
            } else {
                "redirect:/debtors/${debtorId}/publications/new"
            }
        }
    }

    @DeleteMapping("/debtors/{debtorId}/publications/{publicationId}")
    fun delete(
        @PathVariable(DEBTOR_ID) debtorId: Long,
        @PathVariable(PUBLICATION_ID) publicationId: Long,
        redirectAttributes: RedirectAttributes
    ): String = run {
        publicationStorageService.delete(publicationId)
        messageDeleteRecord(redirectAttributes, publicationId)
        "redirect:/debtors/${debtorId}"
    }

    private fun formAttributes(model: Model, publication: PublicationDto, debtorId: Long, isEdit: Boolean) {
        val debtor: DebtorDto = debtorStorageService.getById(debtorId)
        model.addAttribute(PUBLICATION, publication)
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(IS_EDIT, isEdit)
    }
}

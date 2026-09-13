package com.example.newsletters.controller

import com.example.newsletters.dto.model.DocumentTemplateDto
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.service.DocumentTemplateStorageService
import com.example.newsletters.service.ValueListService
import jakarta.validation.Valid
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class DocumentTemplateController(
    val documentTemplateStorageService: DocumentTemplateStorageService,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val documentTemplateTypes = valueListService.getValues("documentTemplateType")

    @GetMapping("/document-templates")
    fun list(
        @RequestParam("search") search: String?,
        @PageableDefault(page = 1, sort = [ID], direction = Sort.Direction.ASC) pageable: Pageable,
        model: Model
    ): String = run {
        val filter = PageFilter(search)
        val documentTemplates = documentTemplateStorageService.getAll(filter, pageableOf(pageable))
        pageAttribute(model, pageable, documentTemplates, filter)
        model.addAttribute(DATA, documentTemplates)
        model.addAttribute(DOCUMENT_TEMPLATE_TYPES, documentTemplateTypes)
        model.addAttribute(FILTER, filter)
        "document-template/list"
    }

    @GetMapping("/document-templates/new")
    fun create(model: Model): String = run {
        formAttributes(model, DocumentTemplateDto(), false)
        "document-template/form"
    }

    @GetMapping("/document-templates/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model): String = run {
        formAttributes(model, documentTemplateStorageService.getById(id), true)
        "document-template/form"
    }

    @PostMapping("/document-templates")
    fun save(
        @Valid @ModelAttribute(DATA) request: DocumentTemplateDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val isUpdate = request.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, request, isUpdate)
            return "document-template/form"
        }

        try {
            val data = documentTemplateStorageService.save(request)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/document-templates/${data.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) "redirect:/document-templates/${request.id}" else "redirect:/document-templates/new"
        }
    }

    @DeleteMapping("/document-templates/{id}")
    fun delete(@PathVariable(ID) id: Long, redirectAttributes: RedirectAttributes): String = run {
        documentTemplateStorageService.delete(id)
        messageDeleteRecord(redirectAttributes, id)
        "redirect:/document-templates"
    }

    private fun formAttributes(model: Model, data: DocumentTemplateDto, isEdit: Boolean) {
        model.addAttribute(DATA, data)
        model.addAttribute(DOCUMENT_TEMPLATE_TYPES, documentTemplateTypes)
        model.addAttribute(IS_EDIT, isEdit)
    }
}

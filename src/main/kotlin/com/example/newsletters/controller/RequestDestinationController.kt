package com.example.newsletters.controller

import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.dto.model.RequestDestinationDto
import com.example.newsletters.entity.enum.DocumentTemplateType
import com.example.newsletters.service.DocumentTemplateStorageService
import com.example.newsletters.service.RequestDestinationService
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
class RequestDestinationController(
    val requestDestinationService: RequestDestinationService,
    val documentTemplateStorageService: DocumentTemplateStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/request-destinations")
    fun list(
        @RequestParam("search") search: String?,
        @PageableDefault(page = DEFAULT_PAGE, sort = [ID], direction = Sort.Direction.ASC) pageable: Pageable,
        model: Model
    ): String = run {
        val filter = PageFilter(search)
        val destinations = requestDestinationService.getAll(filter, pageableOf(pageable))
        pageAttribute(model, pageable, destinations, filter)
        model.addAttribute(DATA, destinations)
        model.addAttribute(FILTER, filter)
        "request-destination/list"
    }

    @GetMapping("/request-destinations/new")
    fun create(model: Model): String = run {
        formAttributes(model, RequestDestinationDto(), false)
        "request-destination/form"
    }

    @GetMapping("/request-destinations/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model): String = run {
        formAttributes(model, requestDestinationService.getById(id), true)
        "request-destination/form"
    }

    @PostMapping("/request-destinations")
    fun save(
        @Valid @ModelAttribute(DATA) request: RequestDestinationDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val isUpdate = request.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, request, isUpdate)
            return "request-destination/form"
        }

        try {
            val data = requestDestinationService.save(request)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/request-destinations/${data.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) "redirect:/request-destinations/${request.id}"
            else "redirect:/request-destinations/new"
        }
    }

    @DeleteMapping("/request-destinations/{id}")
    fun delete(@PathVariable(ID) id: Long, redirectAttributes: RedirectAttributes): String = run {
        requestDestinationService.delete(id)
        messageDeleteRecord(redirectAttributes, id)
        "redirect:/request-destinations"
    }

    private fun formAttributes(model: Model, data: RequestDestinationDto, isEdit: Boolean) {
        model.addAttribute(DATA, data)
        model.addAttribute(
            DOCUMENT_TEMPLATES,
            documentTemplateStorageService.getByType(DocumentTemplateType.REQUEST)
        )
        model.addAttribute(IS_EDIT, isEdit)
    }
}

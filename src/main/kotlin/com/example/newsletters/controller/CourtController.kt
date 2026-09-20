package com.example.newsletters.controller

import com.example.newsletters.dto.model.CourtDto
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.service.CourtService
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
class CourtController(
    val courtService: CourtService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/courts")
    fun list(
        @RequestParam("search") search: String?,
        @PageableDefault(page = DEFAULT_PAGE, sort = [ID], direction = Sort.Direction.ASC) pageable: Pageable,
        model: Model
    ): String = run {
        val filter = PageFilter(search)
        val courts = courtService.getAll(filter, pageableOf(pageable))
        pageAttribute(model, pageable, courts, filter)
        model.addAttribute(DATA, courts)
        model.addAttribute(FILTER, filter)
        "court/list"
    }

    @GetMapping("/courts/new")
    fun create(model: Model): String = run {
        model.addAttribute(DATA, CourtDto())
        model.addAttribute(IS_EDIT, false)
        "court/form"
    }

    @GetMapping("/courts/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model): String = run {
        model.addAttribute(DATA, courtService.getById(id))
        model.addAttribute(IS_EDIT, true)
        "court/form"
    }

    @PostMapping("/courts")
    fun save(
        @Valid @ModelAttribute(DATA) request: CourtDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        var data = request
        val isUpdate = request.id != null
        if (bindingResult.hasErrors()) {
            model.addAttribute(IS_EDIT, isUpdate)
            return "court/form"
        }
        try {
            data = if (isUpdate) courtService.update(request) else courtService.create(request)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
        }
        "redirect:/courts/${data.id}"
    }

    @DeleteMapping("/courts/{id}")
    fun delete(@PathVariable(ID) id: Long, redirectAttributes: RedirectAttributes): String = run {
        courtService.delete(id)
        messageDeleteRecord(redirectAttributes, id)
        "redirect:/courts"
    }
}

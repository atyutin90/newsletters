package com.example.newsletters.controller

import com.example.newsletters.dto.model.ArbitrationManagerDto
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.service.ArbitrationManagerService
import com.example.newsletters.service.DebtorStorageService
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
class ArbitrationManagerController(
    val arbitrationManagerService: ArbitrationManagerService,
    val debtorStorageService: DebtorStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/arbitration-managers")
    fun list(
        @RequestParam("search") search: String?,
        @PageableDefault(page = 1, sort = [ID], direction = Sort.Direction.ASC) pageable: Pageable,
        model: Model
    ): String = run {
        val filter = PageFilter(search)
        val arbitrationManagers = arbitrationManagerService.getAll(filter, pageableOf(pageable))
        pageAttribute(model, pageable, arbitrationManagers, filter)
        model.addAttribute(DATA, arbitrationManagers)
        model.addAttribute(FILTER, filter)
        "arbitration-manager/list"
    }

    @GetMapping("/arbitration-managers/new")
    fun create(model: Model): String = run {
        model.addAttribute(DATA, ArbitrationManagerDto())
        model.addAttribute(DEBTORS, listOf<ArbitrationManagerDto>())
        model.addAttribute(IS_EDIT, false)
        "arbitration-manager/form"
    }

    @GetMapping("/arbitration-managers/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model): String = run {
        val data: ArbitrationManagerDto = arbitrationManagerService.getById(id)
        val debtors = debtorStorageService.getByArbitrationManagerId(id)
        model.addAttribute(DATA, data)
        model.addAttribute(DEBTORS, debtors)
        model.addAttribute(IS_EDIT, true)
        "arbitration-manager/form"
    }

    @PostMapping("/arbitration-managers")
    fun save(
        @Valid @ModelAttribute("request") request: ArbitrationManagerDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        var data: ArbitrationManagerDto = request
        val isUpdate = request.id != null
        if (bindingResult.hasErrors()) {
            model.addAttribute(IS_EDIT, isUpdate)
            return "arbitration-manager/form";
        }
        try {
            if (isUpdate) {
                data = arbitrationManagerService.update(request)
            }
            else data = arbitrationManagerService.create(request)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
        }
        "redirect:/arbitration-managers/${data.id}"
    }

    @DeleteMapping("/arbitration-managers/{id}")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String = run {
        arbitrationManagerService.delete(id)
        messageDeleteRecord(redirectAttributes, id)
        "redirect:/arbitration-managers"
    }
}

package com.example.newsletters.controller

import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.entity.Creditor
import com.example.newsletters.service.CreditorStorageService
import org.springframework.context.MessageSource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.Locale

@Controller
@RequestMapping("/creditor")
class CreditorController(val creditorStorageService: CreditorStorageService, val messageSource: MessageSource) : AbstractController {

    @GetMapping("/all")
    fun getAll(
        @RequestParam(KEYWORD) keyword: String?,
        @RequestParam(defaultValue = DEFAULT_PAGE) page: Int,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) size: Int,
        model: Model
    ): String {
        try {
            val paging: Pageable = PageRequest.of(page - 1, size)
            val debtorDto =
                if (keyword?.isEmpty() != false) creditorStorageService.getAll(paging)
                else creditorStorageService.getByName(keyword, paging)
            model.addAttribute(CREDITORS, debtorDto.content)
            model.addAttribute(CURRENT_PAGE, debtorDto.number + 1)
            model.addAttribute(TOTAL_ITEMS, debtorDto.totalElements)
            model.addAttribute(TOTAL_PAGES, debtorDto.totalPages)
            model.addAttribute(PAGE_SIZE, size)
            model.addAttribute(KEYWORD, keyword)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        return CREDITORS
    }

    @GetMapping("/new")
    fun add(model: Model): String = run {
        model.addAttribute(CREDITOR,  CreditorDto())
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-creditor", arrayOf(), Locale.getDefault()))
        "creditor_form"
    }

    @PostMapping("/save")
    fun save(creditor: CreditorDto, redirectAttributes: RedirectAttributes): String = run {
        try {
            if (creditor.id != null) creditorStorageService.update(creditor)
            else creditorStorageService.create(creditor)
            redirectAttributes.addFlashAttribute(MESSAGE, messageSource.getMessage("record-successfully-created", arrayOf(), Locale.getDefault()))
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/creditor/all"
    }

    @GetMapping("/{id}")
    fun edit(@PathVariable(ID) id: Int, model: Model, redirectAttributes: RedirectAttributes): String =
        try {
            val creditor: CreditorDto = creditorStorageService.getById(id)
            model.addAttribute(CREDITOR, creditor)
            model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-creditor", arrayOf(id), Locale.getDefault()))
            "creditor_form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
            "redirect:/creditor/all"
        }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable(ID) id: Int, model: Model, redirectAttributes: RedirectAttributes): String = run {
        try {
            creditorStorageService.delete(id)
            redirectAttributes.addFlashAttribute(MESSAGE,  messageSource.getMessage("record-successfully-deleted", arrayOf(id), Locale.getDefault()))
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/creditor/all"
    }
}

package com.example.newsletters.controller

import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.dto.RequestDto
import com.example.newsletters.service.RequestStorageService
import org.springframework.context.MessageSource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
@RequestMapping("/request")
class RequestController(val requestStorageService: RequestStorageService, val messageSource: MessageSource) : AbstractController {

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
                if (keyword?.isEmpty() != false) requestStorageService.getAll(paging)
                else requestStorageService.getByCompanyName(keyword, paging)
            model.addAttribute(REQUESTS, debtorDto.content)
            model.addAttribute(CURRENT_PAGE, debtorDto.number + 1)
            model.addAttribute(TOTAL_ITEMS, debtorDto.totalElements)
            model.addAttribute(TOTAL_PAGES, debtorDto.totalPages)
            model.addAttribute(PAGE_SIZE, size)
            model.addAttribute(KEYWORD, keyword)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        return REQUESTS
    }

    @GetMapping("/new")
    fun add(model: Model): String = run {
        model.addAttribute(REQUEST,  CreditorDto())
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-creditor", arrayOf(), Locale.getDefault()))
        "creditor_form"
    }

    @PostMapping("/save")
    fun save(request: RequestDto, redirectAttributes: RedirectAttributes): String = run {
        try {
            if (request.id != null) requestStorageService.update(request)
            else requestStorageService.create(request)
            redirectAttributes.addFlashAttribute(MESSAGE, messageSource.getMessage("record-successfully-created", arrayOf(), Locale.getDefault()))
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/request/all"
    }

    @GetMapping("/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String =
        try {
            val request: RequestDto = requestStorageService.getById(id)
            model.addAttribute(REQUEST, request)
            model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-request", arrayOf(id), Locale.getDefault()))
            "request_form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
            "redirect:/request/all"
        }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String = run {
        try {
            requestStorageService.delete(id)
            redirectAttributes.addFlashAttribute(MESSAGE,  messageSource.getMessage("record-successfully-deleted", arrayOf(id), Locale.getDefault()))
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/request/all"
    }
}

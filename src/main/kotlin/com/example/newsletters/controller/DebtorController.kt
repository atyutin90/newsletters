package com.example.newsletters.controller

import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.service.DebtorStorageService
import org.springframework.context.MessageSource
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/debtor")
class DebtorController(val debtorStorageService: DebtorStorageService, val messageSource: MessageSource) :
    AbstractController {

    @GetMapping("/all")
    fun getAll(model: Model, @Param(KEYWORD) keyword: String?): String = run {
        try {
            val debtors: MutableList<DebtorDto> = mutableListOf()
            if (keyword == null) {
                debtorStorageService.getAll().forEach(debtors::add)
            } else {
                debtorStorageService.getByName(keyword).forEach(debtors::add)
                model.addAttribute(KEYWORD, keyword)
            }
            model.addAttribute(DEBTORS, debtors)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        DEBTORS
    }

    @GetMapping("/new")
    fun add(model: Model): String {
        val debtor = DebtorDto()
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-debtor", arrayOf(), Locale.getDefault()))
        return "debtor_form"
    }

    @PostMapping("/save")
    fun save(debtor: DebtorDto, redirectAttributes: RedirectAttributes): String = run {
        try {
            if (debtor.id != null) debtorStorageService.update(debtor)
            else debtorStorageService.create(debtor)
            redirectAttributes.addFlashAttribute(
                MESSAGE,
                messageSource.getMessage("record-successfully-created", arrayOf(), Locale.getDefault())
            )
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/all"
    }

    @GetMapping("/{id}")
    fun edit(@PathVariable(ID) id: Int, model: Model, redirectAttributes: RedirectAttributes): String =
        try {
            val debtor: DebtorDto = debtorStorageService.getById(id)
            model.addAttribute(DEBTOR, debtor)
            model.addAttribute(
                PAGE_TITLE,
                messageSource.getMessage("update-debtor", arrayOf(id), Locale.getDefault())
            )
            "debtor_form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
            "redirect:/debtor/all"
        }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable(ID) id: Int, model: Model, redirectAttributes: RedirectAttributes): String = run {
        try {
            debtorStorageService.delete(id)
            redirectAttributes.addFlashAttribute(
                MESSAGE,
                messageSource.getMessage("record-successfully-deleted", arrayOf(id), Locale.getDefault())
            )
        } catch (e: java.lang.Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/all"
    }
}

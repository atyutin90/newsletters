package com.example.newsletters.controller

import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.entity.Creditor
import com.example.newsletters.service.CreditorStorageService
import org.springframework.context.MessageSource
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.Locale

@Controller
@RequestMapping("/creditor")
class CreditorController(val creditorStorageService: CreditorStorageService, val messageSource: MessageSource) : AbstractController {

    @GetMapping("/all")
    fun getAll(model: Model, @Param(KEYWORD) keyword: String?): String {
        try {
            val creditors: MutableList<CreditorDto> = mutableListOf()
            if (keyword == null) creditorStorageService.getAll().forEach(creditors::add)
            else creditorStorageService.getByName(keyword).forEach(creditors::add).let { model.addAttribute(KEYWORD, keyword) }
            model.addAttribute(CREDITORS, creditors)
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

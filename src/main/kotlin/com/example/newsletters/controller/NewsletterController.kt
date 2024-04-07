package com.example.newsletters.controller

import com.example.newsletters.dto.NewsletterParams
import com.example.newsletters.dto.TemplateType.NEWSTELLER
import com.example.newsletters.service.CreditorStorageService
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.LocationStorageService
import com.example.newsletters.service.NewsletterService
import com.example.newsletters.service.TemplateStorageService
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import java.util.Locale

@Controller
class NewsletterController(
    val messageSource: MessageSource,
    val templateStorageService: TemplateStorageService,
    val newsletterService: NewsletterService,
    val creditorStorageService: CreditorStorageService,
    val debtorStorageService: DebtorStorageService,
    val locationStorageService: LocationStorageService,
) {

    @GetMapping("/newsletters")
    fun getAll(model: Model): String {
        try {
            model.addAttribute("newsletters", templateStorageService.getByType(NEWSTELLER))
        } catch (e: Exception) {
            model.addAttribute("message", e.message)
        }
        return "newsletter_templates"
    }

    @GetMapping("/newsletters/new")
    fun add(model: Model): String {
        val newsletters = templateStorageService.getByType(NEWSTELLER)
        val creditors = creditorStorageService.getAll()
        val debtors = debtorStorageService.getAll()
        val locations = locationStorageService.getAll()
        model.addAttribute("newsletters", newsletters)
        model.addAttribute("creditors", creditors)
        model.addAttribute("debtors", debtors)
        model.addAttribute("locations", locations)
        model.addAttribute("pageTitle", messageSource.getMessage("create-newsletter", arrayOf(), Locale.getDefault()))
        return "newsletters_form"
    }

    @PostMapping("/newsletters/save")
    fun save(
        newsletterParams: NewsletterParams,
        model: Model
    ): String {
        newsletterService.create(newsletterParams)
        return "redirect:/file/newsletters"
    }
}
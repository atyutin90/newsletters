package com.example.newsletters.controller

import com.example.newsletters.dto.FileDto
import com.example.newsletters.dto.TemplateDto
import com.example.newsletters.dto.TemplateType.NEWSTELLER
import com.example.newsletters.service.TemplateStorageService
import org.springframework.context.MessageSource
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.net.URLEncoder
import java.util.Locale

private const val NEWSLETTERS = "newsletters"

@Controller
@RequestMapping("/template")
class TemplateController(val messageSource: MessageSource, val templateStorageService: TemplateStorageService) {

    @GetMapping("/newsletter/all")
    fun getAll(@Param(KEYWORD) keyword: String?, model: Model): String = run {
        try {
            val newsletters: MutableList<TemplateDto> = mutableListOf()
            if (keyword?.isEmpty() != false ) {
                templateStorageService.getByType(NEWSTELLER).map {newsletters.add(it)}
            } else {
                model.addAttribute(KEYWORD, keyword)
                templateStorageService.getByNameAndType(NEWSTELLER, keyword).map {newsletters.add(it)}
            }
            model.addAttribute(NEWSLETTERS, newsletters)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        "newsletter_templates"
    }

    @GetMapping("/newsletter/new")
    fun add(model: Model): String = run {
        model.addAttribute(NEWSLETTER, FileDto())
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-newsletter-template", arrayOf(), Locale.getDefault()))
        "newsletter_template_form"
    }

    @PostMapping("/newsletter/save")
    fun save(@RequestParam("name") name: String, @RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String = run {
        try {
            templateStorageService.store(name, NEWSTELLER, file)
            redirectAttributes.addFlashAttribute(MESSAGE, messageSource.getMessage("record-successfully-created", arrayOf(), Locale.getDefault()))
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/template/newsletter/all"
    }

    @GetMapping("/newsletter/download/{id}")
    fun get(@PathVariable(ID) id: Int): ResponseEntity<ByteArray> = run {
        val file: TemplateDto? = templateStorageService.get(id)
        val fileName: String = URLEncoder.encode(file?.name, "UTF-8")
        ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$fileName")
            .body(file?.data)
    }

    @GetMapping("/newsletter/delete/{id}")
    fun delete(@PathVariable(ID) id: Int, redirectAttributes: RedirectAttributes): String = run {
        try {
            templateStorageService.delete(id)
            redirectAttributes.addFlashAttribute(MESSAGE,  messageSource.getMessage("record-successfully-deleted", arrayOf(id), Locale.getDefault()))
        } catch (e: java.lang.Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/template/newsletter/all"
    }
}

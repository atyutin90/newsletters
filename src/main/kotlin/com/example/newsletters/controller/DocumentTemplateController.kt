package com.example.newsletters.controller

import com.example.newsletters.dto.DocumentTemplateDto
import com.example.newsletters.service.DocumentTemplateService
import com.example.newsletters.service.ValueListService
import org.springframework.context.MessageSource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
import java.util.Locale.getDefault

@Controller
@RequestMapping("/document-template")
class DocumentTemplateController(
    val documentTemplateService: DocumentTemplateService,
    valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    val documentTemplateTypes = valueListService.getValues("documentTemplateType")

    @GetMapping("/all")
    fun getAll(
        @RequestParam(KEYWORD) keyword: String?,
        @RequestParam(defaultValue = DEFAULT_PAGE) page: Int,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) size: Int,
        model: Model
    ): String {
        try {
            val paging: Pageable = PageRequest.of(page - 1, size)
            val dto =
                if (keyword?.isEmpty() != false) documentTemplateService.getAll(paging)
                else documentTemplateService.getByName(keyword, paging)
            model.addAttribute(DATA, dto.content)
            model.addAttribute(DOCUMENT_TEMPLATE_TYPES, documentTemplateTypes)
            model.addAttribute(CURRENT_PAGE, dto.number + 1)
            model.addAttribute(TOTAL_ITEMS, dto.totalElements)
            model.addAttribute(TOTAL_PAGES, dto.totalPages)
            model.addAttribute(PAGE_SIZE, size)
            model.addAttribute(KEYWORD, keyword)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        return "document-template/list"
    }

    @GetMapping("/new")
    fun add(model: Model): String = run {
        model.addAttribute(DATA,  DocumentTemplateDto())
        model.addAttribute(DOCUMENT_TEMPLATE_TYPES, documentTemplateTypes)
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-document-template", arrayOf(), getDefault()))
        "document-template/form"
    }

    @PostMapping("/save")
    fun save(data: DocumentTemplateDto, @RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String = run {
        try {
            val isUpdate = data.id != null
            documentTemplateService.save(data, file)
           infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/document-template/all"
    }

    @GetMapping("/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String =
        try {
            val data: DocumentTemplateDto = documentTemplateService.getById(id)
            model.addAttribute(DATA, data)
            model.addAttribute(DOCUMENT_TEMPLATE_TYPES, documentTemplateTypes)
            model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-document-template", arrayOf(id), getDefault()))
            "document-template/form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
            "redirect:/document-template/all"
        }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable(ID) id: Long, redirectAttributes: RedirectAttributes): String = run {
        try {
            documentTemplateService.delete(id)
            infoMessageDeleteRecord(redirectAttributes, id)
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/document-template/all"
    }

    @GetMapping("/download/{id}")
    fun download(@PathVariable(ID) id: Long): ResponseEntity<ByteArray> = run {
        val data: DocumentTemplateDto? = documentTemplateService.getById(id)
        val fileName: String = URLEncoder.encode(data?.fileName, "UTF-8")
        ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$fileName")
            .body(data?.data)
    }
}

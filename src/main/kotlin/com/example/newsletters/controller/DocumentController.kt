package com.example.newsletters.controller

import com.example.newsletters.service.DocumentService
import com.example.newsletters.service.DocumentStorageService
import com.example.newsletters.service.logic.DocumentGeneratorService
import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders.CONTENT_DISPOSITION
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.net.URLEncoder

@Controller
@RequestMapping("/debtor/{id}/document")
class DocumentController(
    val documentStorageService: DocumentStorageService,
    val documentGeneratorService: DocumentGeneratorService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/{documentId}/delete")
    fun delete(
        @PathVariable(ID) id: Long,
        @PathVariable(DOCUMENT_ID) documentId: Long,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        try {
            documentStorageService.delete(documentId)
             infoMessageDeleteRecord(redirectAttributes, documentId)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/${id}"
    }

    @GetMapping("/{documentId}/download")
    fun download(
        @PathVariable(ID) id: Long,
        @PathVariable(DOCUMENT_ID) documentId: Long
    ): ResponseEntity<ByteArray> = run {
        val data = documentStorageService.getById(documentId)
        //TODO: добавить проверки
        val documentData = documentGeneratorService.downloadDocument(documentId)
        val fileName: String = URLEncoder.encode(data?.name, "UTF-8")
        ResponseEntity.ok()
            .header(CONTENT_DISPOSITION, "attachment; filename=${fileName}.docx")
            .body(documentData)
    }

    @GetMapping("/generate")
    fun generate(@PathVariable(ID) id: Long) = run {
        documentGeneratorService.generateDocument(id)
        "redirect:/debtor/${id}"
    }
}

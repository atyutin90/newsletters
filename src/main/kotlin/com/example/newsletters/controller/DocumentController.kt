package com.example.newsletters.controller

import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.DocumentTemplateDto
import com.example.newsletters.dto.RequestDestinationDto
import com.example.newsletters.dto.WorkerMeetingDto
import com.example.newsletters.dto.WorkerMeetingParticipantDto
import com.example.newsletters.entity.WorkerMeetingParticipant
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.DocumentStorageService
import com.example.newsletters.service.WorkerMeetingParticipantStorageService
import com.example.newsletters.service.WorkerMeetingStorageService
import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.net.URLEncoder
import java.util.*

@Controller
@RequestMapping("/debtor/{id}/document")
class DocumentController(
    val documentStorageService: DocumentStorageService,
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

    @GetMapping("/download/{documentId}")
    fun download(@PathVariable(ID) id: Long, @PathVariable(DOCUMENT_ID) documentId: Long): ResponseEntity<ByteArray> = run {
        val data = documentStorageService.getById(documentId)
        val fileName: String = URLEncoder.encode(data?.name, "UTF-8")
        ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$fileName")
            .body(data?.data)
    }

    @GetMapping("/generate")
    fun generate(@PathVariable(ID) id: Long) = run {
        "redirect:/debtor/${id}"
    }
}

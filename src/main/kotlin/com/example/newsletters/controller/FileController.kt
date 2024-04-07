package com.example.newsletters.controller

import com.example.newsletters.dto.FileDto
import com.example.newsletters.service.FileStorageService
import org.springframework.context.MessageSource
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.net.URLEncoder
import java.util.*


@Controller
@RequestMapping("/file")
class FileController(val messageSource: MessageSource, val fileStorageService: FileStorageService) : AbstractController {

    @GetMapping("/newsletters")
    fun getAll(
        @RequestParam(KEYWORD) keyword: String?,
        @RequestParam(defaultValue = DEFAULT_PAGE) page: Int,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) size: Int,
        model: Model,
    ): String = run {
        try {
            val paging: Pageable = PageRequest.of(page - 1, size)
            val pageFileDto: Page<FileDto> =
                if (keyword?.isEmpty() != false) fileStorageService.getAllOrderByCreatedAtDesc(paging)
                else fileStorageService.getByNameOrderByCreatedAtDesc(keyword, paging)
            model.addAttribute(FILES, pageFileDto.content);
            model.addAttribute(CURRENT_PAGE, pageFileDto.number + 1)
            model.addAttribute(TOTAL_ITEMS, pageFileDto.totalElements)
            model.addAttribute(TOTAL_PAGES, pageFileDto.totalPages)
            model.addAttribute(PAGE_SIZE, size)
            model.addAttribute(KEYWORD, keyword)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        REPORTS
    }

    @GetMapping("/newsletters/download/{id}")
    fun getFile(@PathVariable(ID) id: Int): ResponseEntity<ByteArray> = run {
        val file: FileDto? = fileStorageService.getById(id)
        val fileName: String = URLEncoder.encode(file?.name, "UTF-8")
        ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$fileName")
            .body(file?.data)
    }

    @GetMapping("/newsletters/delete/{id}")
    fun deleteTutorial(@PathVariable("id") id: Int, redirectAttributes: RedirectAttributes): String {
        try {
            fileStorageService.delete(id)
            redirectAttributes.addFlashAttribute(MESSAGE,  messageSource.getMessage("record-successfully-deleted", arrayOf(id), Locale.getDefault()))
        } catch (e: java.lang.Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        return "redirect:/file/newsletters"
    }
}

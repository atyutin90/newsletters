package com.example.newsletters.controller

import com.example.newsletters.dto.QuestionDto
import com.example.newsletters.dto.RequestDestinationDto
import com.example.newsletters.service.QuestionStorageService
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
import java.util.Locale.getDefault

@Controller
@RequestMapping("/question")
class QuestionController(
    val questionService: QuestionStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

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
                if (keyword?.isEmpty() != false) questionService.getAll(paging)
                else questionService.getByName(keyword, paging)
            model.addAttribute(DATA, dto.content)
            model.addAttribute(CURRENT_PAGE, dto.number + 1)
            model.addAttribute(TOTAL_ITEMS, dto.totalElements)
            model.addAttribute(TOTAL_PAGES, dto.totalPages)
            model.addAttribute(PAGE_SIZE, size)
            model.addAttribute(KEYWORD, keyword)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        return "question/list"
    }

    @GetMapping("/new")
    fun add(model: Model): String = run {
        model.addAttribute(DATA,  QuestionDto())
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-question", arrayOf(), getDefault()))
        "question/form"
    }

    @PostMapping("/save")
    fun save(data: QuestionDto, redirectAttributes: RedirectAttributes): String = run {
        try {
            val isUpdate = data.id != null
            if (isUpdate) questionService.update(data)
            else questionService.create(data)
           infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/question/all"
    }

    @GetMapping("/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String =
        try {
            val data: QuestionDto = questionService.getById(id)
            model.addAttribute(DATA, data)
            model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-question", arrayOf(id), getDefault()))
            "question/form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
            "redirect:/question/all"
        }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String = run {
        try {
            questionService.delete(id)
            infoMessageDeleteRecord(redirectAttributes, id)
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/question/all"
    }
}

package com.example.newsletters.controller

import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.dto.model.QuestionDto
import com.example.newsletters.entity.enum.DocumentTemplateType
import com.example.newsletters.service.DocumentTemplateStorageService
import com.example.newsletters.service.QuestionStorageService
import jakarta.validation.Valid
import org.springframework.context.MessageSource
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class QuestionController(
    val questionService: QuestionStorageService,
    val documentTemplateStorageService: DocumentTemplateStorageService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/questions")
    fun list(
        @RequestParam("search") search: String?,
        @PageableDefault(page = DEFAULT_PAGE, sort = ["position"], direction = Sort.Direction.ASC) pageable: Pageable,
        model: Model
    ): String = run {
        val filter = PageFilter(search)
        val questions = questionService.getAll(filter, pageableOf(pageable))
        pageAttribute(model, pageable, questions, filter)
        model.addAttribute(DATA, questions)
        model.addAttribute(FILTER, filter)
        "question/list"
    }

    @GetMapping("/questions/new")
    fun create(model: Model): String = run {
        formAttributes(model, QuestionDto(), false)
        "question/form"
    }

    @GetMapping("/questions/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model): String = run {
        formAttributes(model, questionService.getById(id), true)
        "question/form"
    }

    @PostMapping("/questions")
    fun save(
        @Valid @ModelAttribute(DATA) request: QuestionDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val isUpdate = request.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, request, isUpdate)
            return "question/form"
        }

        try {
            val data = questionService.save(request)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/questions/${data.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) "redirect:/questions/${request.id}" else "redirect:/questions/new"
        }
    }

    @DeleteMapping("/questions/{id}")
    fun delete(@PathVariable(ID) id: Long, redirectAttributes: RedirectAttributes): String = run {
        questionService.delete(id)
        messageDeleteRecord(redirectAttributes, id)
        "redirect:/questions"
    }

    private fun formAttributes(model: Model, data: QuestionDto, isEdit: Boolean) {
        model.addAttribute(DATA, data)
        model.addAttribute(
            DOCUMENT_TEMPLATES,
            documentTemplateStorageService.getByType(DocumentTemplateType.BULLETIN)
        )
        model.addAttribute(IS_EDIT, isEdit)
    }
}

package com.example.newsletters.controller

import com.example.newsletters.dto.ArbitrationManagerDto
import com.example.newsletters.dto.CourtDto
import com.example.newsletters.dto.RequestDestinationDto
import com.example.newsletters.service.ArbitrationManagerService
import com.example.newsletters.service.CourtService
import com.example.newsletters.service.DebtorStorageService
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
import java.util.Locale

@Controller
@RequestMapping("/arbitration-manager")
class ArbitrationManagerController(
    val arbitrationManagerService: ArbitrationManagerService,
    val debtorStorageService: DebtorStorageService,
    override val messageSource: MessageSource
): AbstractController(messageSource) {

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
                if (keyword?.isEmpty() != false) arbitrationManagerService.getAll(paging)
                else arbitrationManagerService.getByFullName(keyword, paging)
            model.addAttribute(DATA, dto.content)
            model.addAttribute(CURRENT_PAGE, dto.number + 1)
            model.addAttribute(TOTAL_ITEMS, dto.totalElements)
            model.addAttribute(TOTAL_PAGES, dto.totalPages)
            model.addAttribute(PAGE_SIZE, size)
            model.addAttribute(KEYWORD, keyword)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        return "arbitration-manager/list"
    }

    @GetMapping("/new")
    fun add(model: Model): String = run {
        model.addAttribute(DATA,  ArbitrationManagerDto())
        model.addAttribute(DEBTORS, listOf<ArbitrationManagerDto>())
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-arbitration-manager", arrayOf(), Locale.getDefault()))
        "arbitration-manager/form"
    }

    @PostMapping("/save")
    fun save(request: ArbitrationManagerDto, redirectAttributes: RedirectAttributes): String = run {
        try {
            val isUpdate = request.id != null
            if (isUpdate) arbitrationManagerService.update(request)
            else arbitrationManagerService.create(request)
            infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.message)
        }
        "redirect:/arbitration-manager/all"
    }

    @GetMapping("/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String =
        try {
            val data: ArbitrationManagerDto = arbitrationManagerService.getById(id)
            val debtors = debtorStorageService.getByArbitrationManagerId(id)
            model.addAttribute(DATA, data)
            model.addAttribute(DEBTORS, debtors)
            model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-arbitration-manager", arrayOf(id), Locale.getDefault()))
            "arbitration-manager/form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
            "redirect:/arbitration-manager/all"
        }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String = run {
        try {
            arbitrationManagerService.delete(id)
            infoMessageDeleteRecord(redirectAttributes, id)
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/arbitration-manager/all"
    }
}

package com.example.newsletters.controller

import com.example.newsletters.annotation.ValueList
import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.DebtorMeetingDto
import com.example.newsletters.dto.PublicationDto
import com.example.newsletters.dto.RequestDestinationDto
import com.example.newsletters.dto.RequestDto
import com.example.newsletters.dto.WorkerMeetingDto
import com.example.newsletters.entity.enum.ClientType
import com.example.newsletters.service.ArbitrationManagerService
import com.example.newsletters.service.CreditorStorageService
import com.example.newsletters.service.DebtorMeetingStorageService
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.PublicationStorageService
import com.example.newsletters.service.RequestDestinationService
import com.example.newsletters.service.RequestStorageService
import com.example.newsletters.service.ValueListService
import com.example.newsletters.service.WorkerMeetingStorageService
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
import java.util.*

@Controller
@RequestMapping("/debtor")
class DebtorController(
    val debtorStorageService: DebtorStorageService,
    val requestStorageService: RequestStorageService,
    val publicationStorageService: PublicationStorageService,
    val requestDestinationService: RequestDestinationService,
    val debtorMeetingService: DebtorMeetingStorageService,
    val workerMeetingService: WorkerMeetingStorageService,
    val creditorStorageService: CreditorStorageService,
    val arbitrationManagerService: ArbitrationManagerService,
    val valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/all")
    fun getAll(
        @RequestParam(KEYWORD) keyword: String?,
        @RequestParam(defaultValue = DEFAULT_PAGE) page: Int,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) size: Int,
        model: Model
    ): String = run {
        try {
            val paging: Pageable = PageRequest.of(page - 1, size)
            val debtorDto =
                if (keyword?.isEmpty() != false) debtorStorageService.getAll(paging)
                else debtorStorageService.getByName(keyword, paging)
            model.addAttribute(DEBTORS, debtorDto.content)
            model.addAttribute(CURRENT_PAGE, debtorDto.number + 1)
            model.addAttribute(TOTAL_ITEMS, debtorDto.totalElements)
            model.addAttribute(TOTAL_PAGES, debtorDto.totalPages)
            model.addAttribute(PAGE_SIZE, size)
            model.addAttribute(KEYWORD, keyword)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        "debtor/list"
    }

    @GetMapping("/new")
    fun add(model: Model): String {
        model.addAttribute(DEBTOR, DebtorDto())
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-debtor", arrayOf(), Locale.getDefault()))
        return "debtor/form"
    }

    @PostMapping("/save", params = ["save"])
    fun save(debtor: DebtorDto, redirectAttributes: RedirectAttributes): String = run {
        try {
            val isUpdate = debtor.id != null
            if (isUpdate) debtorStorageService.update(debtor)
            else debtorStorageService.create(debtor)
            infoMessageCreateOrUpdateRecord(redirectAttributes, isUpdate)
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/all"
    }

    @GetMapping("/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String =
        try {
            val debtor: DebtorDto = debtorStorageService.getById(id)
            val requestDtoList = requestStorageService.getByDebtorId(debtor.id!!)
            val publicationDtoList = publicationStorageService.getByDebtorId(debtor.id)
            val debtorMeetingList = debtorMeetingService.getByDebtorId(debtor.id)
            val workerMeetingList = workerMeetingService.getByDebtorId(debtor.id)
            val creditorDtoList = creditorStorageService.getByDebtorId(debtor.id)
            val clientTypes = valueListService.getValues("clientType")
            val requestDestinations: List<RequestDestinationDto> = requestDestinationService.getAll()
            val arbitrationManagers = arbitrationManagerService.getAll()
            model.addAttribute(DEBTOR, debtor)
            model.addAttribute(REQUEST, RequestDto(debtorId = id))
            model.addAttribute(REQUESTS, requestDtoList)
            model.addAttribute(PUBLICATION, PublicationDto(debtorId = id))
            model.addAttribute(PUBLICATIONS, publicationDtoList)
            model.addAttribute(CREDITOR, CreditorDto(debtorId = id))
            model.addAttribute(CREDITORS, creditorDtoList)
            model.addAttribute(CLIENT_TYPES, clientTypes)
            model.addAttribute(DEBTOR_MEETING, DebtorMeetingDto(debtorId = id))
            model.addAttribute(DEBTOR_MEETINGS, debtorMeetingList)
            model.addAttribute(WORKER_MEETING, WorkerMeetingDto(debtorId = id))
            model.addAttribute(WORKER_MEETINGS, workerMeetingList)
            model.addAttribute(REQUEST_DESTINATIONS, requestDestinations)
            model.addAttribute(ARBITRATION_MANAGERS, arbitrationManagers)
            model.addAttribute(PAGE_TITLE, messageSource.getMessage("update-debtor", arrayOf(id), Locale.getDefault()))
            "debtor/form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
            "redirect:/debtor/all"
        }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable(ID) id: Long, model: Model, redirectAttributes: RedirectAttributes): String = run {
        try {
            debtorStorageService.delete(id)
            infoMessageDeleteRecord(redirectAttributes, id)
        } catch (e: java.lang.Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/debtor/all"
    }
}

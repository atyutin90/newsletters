package com.example.newsletters.controller

import com.example.newsletters.dto.model.CreditorDto
import com.example.newsletters.dto.model.DebtorDto
import com.example.newsletters.dto.model.DebtorMeetingDto
import com.example.newsletters.dto.model.DocumentDto
import com.example.newsletters.dto.model.DocumentGroupDto
import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.dto.model.PublicationDto
import com.example.newsletters.dto.model.RequestDestinationDto
import com.example.newsletters.dto.model.RequestDto
import com.example.newsletters.dto.model.WorkerMeetingDto
import com.example.newsletters.service.ArbitrationManagerService
import com.example.newsletters.service.CreditorStorageService
import com.example.newsletters.service.DebtorMeetingStorageService
import com.example.newsletters.service.DebtorStorageService
import com.example.newsletters.service.DocumentStorageService
import com.example.newsletters.service.PublicationStorageService
import com.example.newsletters.service.RequestDestinationService
import com.example.newsletters.service.RequestStorageService
import com.example.newsletters.service.ValueListService
import com.example.newsletters.service.WorkerMeetingStorageService
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
import java.util.Locale.getDefault

@Controller
class DebtorController(
    val debtorStorageService: DebtorStorageService,
    val requestStorageService: RequestStorageService,
    val publicationStorageService: PublicationStorageService,
    val requestDestinationService: RequestDestinationService,
    val debtorMeetingService: DebtorMeetingStorageService,
    val workerMeetingService: WorkerMeetingStorageService,
    val creditorStorageService: CreditorStorageService,
    val documentStorageService: DocumentStorageService,
    val arbitrationManagerService: ArbitrationManagerService,
    val valueListService: ValueListService,
    override val messageSource: MessageSource
) : AbstractController(messageSource) {

    @GetMapping("/debtors")
    fun list(
        @RequestParam("search") search: String?,
        @PageableDefault(page = DEFAULT_PAGE, sort = [ID], direction = Sort.Direction.ASC) pageable: Pageable,
        model: Model
    ): String = run {
        val filter = PageFilter(search)
        val debtors = debtorStorageService.getAll(filter, pageableOf(pageable))
        pageAttribute(model, pageable, debtors, filter)
        model.addAttribute(DATA, debtors)
        model.addAttribute(FILTER, filter)
        "debtor/list"
    }

    @GetMapping("/debtors/new")
    fun create(model: Model): String = run {
        formAttributes(model, DebtorDto(), false)
        "debtor/form"
    }

    @GetMapping("/debtors/{id}")
    fun edit(@PathVariable(ID) id: Long, model: Model): String = run {
        formAttributes(model, debtorStorageService.getById(id), true)
        "debtor/form"
    }

    @PostMapping("/debtors")
    fun save(
        @Valid @ModelAttribute(DEBTOR) request: DebtorDto,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String = run {
        val isUpdate = request.id != null
        if (bindingResult.hasErrors()) {
            formAttributes(model, request, isUpdate)
            return "debtor/form"
        }

        try {
            val debtor = if (isUpdate) debtorStorageService.update(request) else debtorStorageService.create(request)
            messageCreateOrUpdateRecord(redirectAttributes, isUpdate)
            "redirect:/debtors/${debtor.id}"
        } catch (ex: Exception) {
            redirectAttributes.addFlashAttribute(ERROR, ex.message)
            if (isUpdate) "redirect:/debtors/${request.id}" else "redirect:/debtors/new"
        }
    }

    @DeleteMapping("/debtors/{id}")
    fun delete(@PathVariable(ID) id: Long, redirectAttributes: RedirectAttributes): String = run {
        debtorStorageService.delete(id)
        messageDeleteRecord(redirectAttributes, id)
        "redirect:/debtors"
    }

    private fun formAttributes(model: Model, debtor: DebtorDto, isEdit: Boolean) {
        model.addAttribute(DEBTOR, debtor)
        model.addAttribute(ARBITRATION_MANAGERS, arbitrationManagerService.getAll())
        model.addAttribute(IS_EDIT, isEdit)

        if (!isEdit || debtor.id == null) return

        val id = debtor.id
        model.addAttribute(REQUEST, RequestDto(debtorId = id))
        model.addAttribute(REQUESTS, requestStorageService.getByDebtorId(id))
        model.addAttribute(PUBLICATION, PublicationDto(debtorId = id))
        model.addAttribute(PUBLICATIONS, publicationStorageService.getByDebtorId(id))
        model.addAttribute(CREDITOR, CreditorDto(debtorId = id))
        model.addAttribute(CREDITORS, creditorStorageService.getByDebtorId(id))
        model.addAttribute(CLIENT_TYPES, valueListService.getValues("clientType"))
        model.addAttribute(DEBTOR_MEETING, DebtorMeetingDto(debtorId = id))
        model.addAttribute(DEBTOR_MEETINGS, debtorMeetingService.getByDebtorId(id))
        model.addAttribute(WORKER_MEETING, WorkerMeetingDto(debtorId = id))
        model.addAttribute(WORKER_MEETINGS, workerMeetingService.getByDebtorId(id))
        val requestDestinations: List<RequestDestinationDto> = requestDestinationService.getAll()
        model.addAttribute(REQUEST_DESTINATIONS, requestDestinations)
        val documents = documentStorageService.getByDebtorId(id)
        model.addAttribute(DOCUMENTS, documents)
        model.addAttribute(DOCUMENT_GROUPS, groupDocumentsByType(documents))
    }

    private fun groupDocumentsByType(documents: List<DocumentDto>): List<DocumentGroupDto> {
        val documentsByType = documents.groupBy { it.type }
        val documentTypes = valueListService.getValues("documentTemplateType")
        val knownTypes = documentTypes.map { it.code }.toSet()

        val knownGroups = documentTypes.mapNotNull { type ->
            documentsByType[type.code]?.let { DocumentGroupDto(type.code, type.value, it) }
        }
        val unknownGroups = documentsByType
            .filterKeys { it !in knownTypes }
            .map { (type, typedDocuments) ->
                DocumentGroupDto(
                    type = type,
                    name = type ?: messageSource.getMessage("debtor.document.unknown-type", null, getDefault()),
                    documents = typedDocuments,
                )
            }

        return knownGroups + unknownGroups
    }
}

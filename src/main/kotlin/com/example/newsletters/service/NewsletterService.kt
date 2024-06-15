package com.example.newsletters.service

import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.FileDto
import com.example.newsletters.dto.LocationDto
import com.example.newsletters.dto.NewsletterParams
import com.example.newsletters.dto.TemplateDto
import com.example.newsletters.utils.FileTemplateUtils.generateTemplate
import com.example.newsletters.utils.FileUtils.getExtensionFileByName
import com.example.newsletters.utils.FileUtils.getFileByNameWithoutExtension

import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

@Service
class NewsletterService(
    private val templateService: TemplateStorageService,
    private val creditorService: CreditorStorageService,
    private val debtorService: DebtorStorageService,
    private val locationService: LocationStorageService,
    private val fileStorageService: FileStorageService
) {

    fun create(newsletterParams: NewsletterParams) {
        val templates = templateService.getByIds(newsletterParams.templateIds.mapNotNull { it.toIntOrNull() })
        val creditors = creditorService.getByIds(newsletterParams.creditorIds.mapNotNull { it.toIntOrNull() })
        val debtors = debtorService.getByIds(newsletterParams.debtorIds.mapNotNull { it.toIntOrNull() })
        val locations = locationService.getByIds(newsletterParams.locationIds.mapNotNull { it.toIntOrNull() })

        creditors.forEach { c ->
            debtors.forEach { d ->
                locations.forEach { l ->
                    templates.forEach { t ->
                        val map = mapObject(c).plus(mapObject(l).plus(mapObject(d))).plus("date" to newsletterParams.date)
                        val outputStream = generateTemplate(ByteArrayInputStream(t.data), map)
                        val extensionFile = t.name?.let { getExtensionFileByName(it) }
                        val reportName = t.name?.let { "${getFileByNameWithoutExtension(it)}_${c.name}_${d.name}.${extensionFile}" }.orEmpty()
                        fileStorageService.store(
                            reportName,
                            FileDto(name = reportName, type = t.contentType, data = outputStream.toByteArray())
                        )
                    }
                }
            }
        }
    }

    private fun <T> mapObject(value: T): Map<String, Any> = when (value) {
            is TemplateDto -> mapOf("template" to value)
            is CreditorDto -> mapOf("creditor" to value)
            is LocationDto -> mapOf("location" to value)
            is DebtorDto ->  mapOf("debtor" to value)
            else -> mapOf()
        }
}

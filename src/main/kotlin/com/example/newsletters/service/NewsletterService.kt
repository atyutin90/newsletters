package com.example.newsletters.service

import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.FileDto
import com.example.newsletters.dto.LocationDto
import com.example.newsletters.dto.NewsletterParams
import com.example.newsletters.dto.TemplateDto
import com.example.newsletters.utils.FileUtils.getExtensionFileByName
import com.example.newsletters.utils.FileUtils.getFileByNameWithoutExtension
import com.example.newsletters.utils.ObjectUtils.toMap
import de.phip1611.Docx4JSRUtil
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
                        val map = mapObject(c).plus(mapObject(l).plus(mapObject(d))).plus("\${date}" to newsletterParams.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
                        val template = WordprocessingMLPackage.load(ByteArrayInputStream(t.data))
                        Docx4JSRUtil.searchAndReplace(template, map)
                        val outputStream = ByteArrayOutputStream()
                        template.save(outputStream)
                        val extensionFile = t.name?.let { getExtensionFileByName(it) }
                        val reportName =
                            t.name?.let { "${getFileByNameWithoutExtension(it)}_${c.name}_${d.name}.${extensionFile}" }.orEmpty()
                        fileStorageService.store(
                            reportName,
                            FileDto(name = reportName, type = t.contentType, data = outputStream.toByteArray())
                        )
                    }
                }
            }
        }
    }

    private fun <T> mapObject(value: T): Map<String, String> = run {
        fun <T> T.toMap(name: String): Map<String, String> = this?.toMap()?.map { "\${${name}.${it.key}}" to it.value }?.associate { it.first to it.second } ?: mapOf()
        when (value) {
            is TemplateDto -> value.toMap("template")
            is CreditorDto -> value.toMap("creditor")
            is LocationDto -> value.toMap("location")
            is DebtorDto -> value.toMap("debtor")
            else -> mapOf()
        }
    }
}

package com.example.newsletters.utils

import com.example.newsletters.utils.FreemarkerUtils.render
import de.phip1611.Docx4JSRUtil
import de.phip1611.Docx4JSRUtil.getAllElementsOfType
import de.phip1611.Docx4JSRUtil.getCompleteString
import org.apache.commons.lang3.StringUtils.EMPTY
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.Text
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

object FileTemplateUtils {

    fun generateTemplate(inputStream: ByteArrayInputStream, map: Map<String, Any>): ByteArrayOutputStream {
        val template = WordprocessingMLPackage.load(inputStream)
        val outputStream = ByteArrayOutputStream()
        Docx4JSRUtil.searchAndReplace(template, getParamMap(template.freemarkerParams, map))
        template.save(outputStream)
        return outputStream
    }

    private val WordprocessingMLPackage.freemarkerParams : Set<String> get() = run {
        // Регулярное выражение для поиска блоков, начинающихся с ${ и заканчивающихся }
        val regex = "(?:<#list[\\s\\S]*?<\\/#list>|\\$\\{([^}]*)\\})"
        val text = getCompleteString(getAllElementsOfType(mainDocumentPart, Text::class.java))
        val templateParams = mutableSetOf<String>()
        val pattern = Pattern.compile(regex)
        pattern.matcher(text).let { matcher ->
            while (matcher.find()) {
                templateParams.add(matcher.group())
            }
        }
        templateParams
    }

    //TODO: Подумать над тем, что возвращать, если значение не задано
    private fun getParamMap(templateParams: Set<String>, map: Map<String, Any>): Map<String, String> =
        templateParams.associateWith { param -> (param.render(map) ?: EMPTY ) }
}

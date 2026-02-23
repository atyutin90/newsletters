package com.example.newsletters.service

import com.example.newsletters.annotation.ValueList
import com.example.newsletters.dto.model.CodeAndValue
import org.reflections.Reflections
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

private const val PACKAGE = "com.example.newsletters.entity.enum"
@Service
class ValueListService(private val messageSource: MessageSource) {
    private val locale: Locale = Locale.getDefault()
    private val classMap: Map<String, Class<out Enum<*>>> = getAnnotatedEnumsMap()

    fun getValues(type: String): List<CodeAndValue> =
        if (classMap.containsKey(type)) classMap[type]?.let { getFromMessageSource(it) } ?: listOf()
        else listOf()

    private fun getFromMessageSource(enumClass: Class<out Enum<*>>): List<CodeAndValue> =
        enumClass.enumConstants
            .mapNotNull { v -> getHeader(v)?.let { CodeAndValue(v.name, it) } }
            .toList()

    private fun getHeader(enumValue: Enum<*>): String? = runCatching {
        val resourceId = enumValue.javaClass.simpleName + "." + enumValue.name
        messageSource.getMessage(resourceId, null, locale)
    }.getOrNull()

    private fun getAnnotatedEnumsMap(): Map<String, Class<out Enum<*>>> = run {
        val reflections = Reflections(PACKAGE)
        reflections.getTypesAnnotatedWith(ValueList::class.java)
            .filter { it.isEnum }
            .mapNotNull { it as? Class<out Enum<*>?> }
            .associateBy { it.getAnnotation(ValueList::class.java).type }
    }
}

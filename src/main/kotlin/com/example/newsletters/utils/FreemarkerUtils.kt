package com.example.newsletters.utils

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import io.github.oshai.kotlinlogging.KotlinLogging
import no.api.freemarker.java8.Java8ObjectWrapper
import org.apache.commons.codec.CharEncoding.UTF_8
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter

private val log = KotlinLogging.logger {}

object FreemarkerUtils {

    private const val DEFAULT_TEMPLATE_NAME = "Default Template"
    private val configuration = Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).apply {
        defaultEncoding = UTF_8;
        objectWrapper = Java8ObjectWrapper(this.incompatibleImprovements)
    }

    /**
     * Создание текстового представление шаблона и параметров шаблона.
     *
     * @param params параметров шаблона
     * @return текстовое представление шаблона
     */
    fun String.render(params: Map<String, Any>): String? = try {
        val writer = StringWriter()
        val template = Template(DEFAULT_TEMPLATE_NAME, StringReader(this), configuration)
        template.process(params, writer)
        writer.toString()
    } catch (ex: IOException) {
        log.error(ex) { "Can't load template from string $this" }; null
    } catch (ex: TemplateException) {
        log.error(ex) { "Can't process template for string $this" }; null
    }
}

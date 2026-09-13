package com.example.newsletters.service

import com.example.newsletters.exceptions.DataNotFoundException
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder.getLocale

abstract class AbstractRepositoryService(private val messageSource: MessageSource) {

    /**
     * Бросает {@link DataNotFoundException} с локализованным сообщением
     * об отсутствии данных по указанному идентификатору.
     *
     * @param id идентификатор записи; может быть {@code null}
     * @throws DataNotFoundException всегда
     */
    open fun notExist(id: Long?): Nothing {
        throw DataNotFoundException(
            messageSource.getMessage(
                "error.not-found-data-with-id",
                id?.let { arrayOf(id) } ?: arrayOf(),
                getLocale()
            )
        )
    }
}
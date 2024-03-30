package com.example.newsletters.configuraton

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*


@Configuration
class I18NConfig {
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setFallbackToSystemLocale(false)
        messageSource.setBasenames("classpath:locale/messages")
        return messageSource
    }

    @Bean
    fun localeResolver(): LocaleResolver = run {
        SessionLocaleResolver().apply { setDefaultLocale(Locale("ru", "RU"))}
    }
}
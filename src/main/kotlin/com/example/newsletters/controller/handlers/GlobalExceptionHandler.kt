package com.example.newsletters.controller.handlers

import com.example.newsletters.exceptions.DataNotFoundException
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder.getLocale
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView

@ControllerAdvice
class GlobalExceptionHandler(val messageSource: MessageSource) {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(DataNotFoundException::class)
    fun handeDataNotFoundException(e: DataNotFoundException): ModelAndView {
        val modelAndView = ModelAndView("error/error")
        modelAndView.addObject("message", e.message)
        modelAndView.addObject("status", NOT_FOUND.value())
        return modelAndView
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleException(): ModelAndView {
        val modelAndView = ModelAndView("error/error")
        modelAndView.addObject(
            "message",
            messageSource.getMessage("error.internal-error", arrayOf(), getLocale())
        )
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value())
        return modelAndView
    }
}
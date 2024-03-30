package com.example.newsletters.controller

import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class BaseController(val messageSource: MessageSource) {

    @GetMapping("/")
    fun get(model: Model): String {
        model.addAttribute("greetingMessage", messageSource.getMessage("greeting-message", arrayOf(), Locale.getDefault()))
        model.addAttribute("greetingAction", messageSource.getMessage("greeting-action", arrayOf(), Locale.getDefault()))
        return "main"
    }
}
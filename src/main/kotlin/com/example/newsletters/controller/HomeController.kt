package com.example.newsletters.controller

import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController(val messageSource: MessageSource) {

    @GetMapping("/")
    fun get(model: Model): String {
        return "home/edit"
    }
}
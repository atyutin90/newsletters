package com.example.newsletters.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AuthPageController {
    @GetMapping("/login")
    fun login(): String = "auth/login"
}

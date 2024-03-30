package com.example.newsletters.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class NewsletterParams(
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val date: LocalDate,
    val templateIds: List<String> = listOf(),
    val creditorIds: List<String> = listOf(),
    val debtorIds: List<String> = listOf(),
    val locationIds: List<String> = listOf(),
)

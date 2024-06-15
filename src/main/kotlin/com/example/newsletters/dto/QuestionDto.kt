package com.example.newsletters.dto

data class QuestionDto(
    val id: Long? = null,
    val value: String? = null,
    val position: Int? = null,
    val documentTemplateIds: Set<Long> = setOf(),
)

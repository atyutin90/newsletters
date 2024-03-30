package com.example.newsletters.dto

data class TemplateDto(
    val id: Int? = null,
    val name: String? = null,
    val type: TemplateType? = null,
    val contentType: String? = null,
    val data: ByteArray? = null,
)

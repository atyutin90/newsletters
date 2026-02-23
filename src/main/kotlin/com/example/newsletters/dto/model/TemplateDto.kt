package com.example.newsletters.dto.model

data class TemplateDto(
    val id: Long? = null,
    val name: String? = null,
    val type: TemplateType? = null,
    val contentType: String? = null,
    val data: ByteArray? = null,
)

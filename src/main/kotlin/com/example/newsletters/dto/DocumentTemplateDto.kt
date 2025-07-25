package com.example.newsletters.dto

data class DocumentTemplateDto(
    val id: Long? = null,
    val name: String? = null,
    val fileName: String? = null,
    val type: String? = null,
    val contentType: String? = null,
    val data: ByteArray? = null,
)

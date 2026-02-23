package com.example.newsletters.dto.model

data class DocumentDto(
    val id: Long? = null,
    val debtorId: Long,
    val name: String? = null,
    val type: String? = null,
    val templateResourcePath: String? = null,
)

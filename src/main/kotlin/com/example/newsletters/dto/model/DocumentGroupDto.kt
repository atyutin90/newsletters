package com.example.newsletters.dto.model

data class DocumentGroupDto(
    val type: String?,
    val name: String,
    val documents: List<DocumentDto>,
)

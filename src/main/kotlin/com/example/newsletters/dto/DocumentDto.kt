package com.example.newsletters.dto

data class DocumentDto(
    val id: Long? = null,
    val debtorId: Long,
    val name: String? = null,
    val fileName: String? = null,
    val type: String? = null,
    val contentType: String? = null,
    val data: ByteArray? = null,
)

package com.example.newsletters.dto

import java.time.ZonedDateTime

data class FileDto(
    val id: Int? = null,
    val name: String? = null,
    val createdAt: ZonedDateTime? = null,
    val fileName: String? = null,
    val type: String? = null,
    val data: ByteArray? = null,
)
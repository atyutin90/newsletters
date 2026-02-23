package com.example.newsletters.dto.model

data class RequestDestinationDto(
    val id: Long? = null,
    val name: String? = null,
    val enabled: Boolean? = null,
    val documentTemplateId: Long? = null,
)

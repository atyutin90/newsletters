package com.example.newsletters.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

object ObjectUtils {
    @Throws(IllegalAccessException::class)
    fun Any.toMap(): Map<String, String> = ObjectMapper().convertValue(this,  object : TypeReference<Map<String, String>>() {})
}

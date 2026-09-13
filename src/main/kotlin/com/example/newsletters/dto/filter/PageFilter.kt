package com.example.newsletters.dto.model

import com.example.newsletters.controller.SEARCH
import java.net.URLEncoder.encode
import kotlin.text.Charsets.UTF_8

data class PageFilter(val search: String?) {

    fun buildExtraQuery(): String {
        val query = StringBuilder()
        appendQueryParam(query, SEARCH, search)
        return query.toString()
    }

    fun appendQueryParam(query: StringBuilder, name: String, value: Any?) {
        if (value != null && value.toString().isNotEmpty()) {
            query.append('&')
                .append(name)
                .append('=')
                .append(encode(value.toString(), UTF_8));
        }
    }
}

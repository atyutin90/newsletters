package com.example.newsletters.entity.enum

enum class ClientType {
    INDIVIDUAL,
    LEGAL;

    companion object {
        fun clientTypeOf(value: String?): ClientType? =
            if (value != null) entries.firstOrNull { it.name.equals(value, true) } else null
    }
}

package com.example.newsletters.entity.enum

import com.example.newsletters.annotation.ValueList

@ValueList("documentTemplateType")
enum class DocumentTemplateType(val multiple: Boolean) {
    BULLETIN(true),
    WORKER_NOTIFICATION(false),
    CREDITOR_NOTIFICATION(false),
    REGISTRATION_CREDITOR_JOURNAL(false),
    REGISTRATION_WORKER_JOURNAL(false),
    REQUEST(true);

    companion object {
        fun documentTemplateTypeOf(value: String?): DocumentTemplateType? =
            if (value != null) entries.firstOrNull { it.name.equals(value, true) } else null
    }
}

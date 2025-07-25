package com.example.newsletters.entity.enum

import com.example.newsletters.annotation.ValueList

@ValueList("documentTemplateType")
enum class DocumentTemplateType(val multiple: Boolean) {
    REQUEST(true),
    BULLETIN(true),
    NOTIFICATION_1(false),
    NOTIFICATION_2(false),
    REGISTRATION_WORKER_JOURNAL(false),
    REGISTRATION_CREDITOR_JOURNAL(false);

    companion object {
        fun documentTemplateTypeOf(value: String?): DocumentTemplateType? =
            if (value != null) entries.firstOrNull { it.name.equals(value, true) } else null
    }
}

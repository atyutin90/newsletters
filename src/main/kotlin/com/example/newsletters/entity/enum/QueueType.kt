package com.example.newsletters.entity.enum

import com.example.newsletters.annotation.ValueList

@ValueList("queueType")
enum class QueueType {
    FIRST,
    FIRST_REPAYMENT,
    SECOND,
    SECOND_REPAYMENT,
    THIRD,
    THIRD_REPAYMENT,
    THIRD_DEPOSIT,
    THIRD_REPAYMENT_DEPOSIT;

    companion object {
        fun queueTypeOf(value: String?): QueueType? =
            if (value != null) QueueType.entries.firstOrNull { it.name.equals(value, true) } else null
    }
}
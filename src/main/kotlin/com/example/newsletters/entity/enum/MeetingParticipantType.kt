package com.example.newsletters.entity.enum

import com.example.newsletters.annotation.ValueList

@ValueList("meetingParticipantType")
enum class MeetingParticipantType {
    CREDITOR,
    DEBTOR,
    PARTICIPANT;

    companion object {
        fun meetingParticipantTypeOf(value: String?): MeetingParticipantType? =
            if (value != null) entries.firstOrNull { it.name.equals(value, true) } else null
    }
}

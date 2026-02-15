package com.example.newsletters.entity.converters

import com.example.newsletters.entity.enum.MeetingParticipantType
import com.example.newsletters.entity.enum.MeetingParticipantType.Companion.meetingParticipantTypeOf
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class MeetingParticipantTypeConverter : AttributeConverter<MeetingParticipantType?, String?> {

    override fun convertToDatabaseColumn(type: MeetingParticipantType?): String? = type?.name

    override fun convertToEntityAttribute(value: String?): MeetingParticipantType? = meetingParticipantTypeOf(value)
}

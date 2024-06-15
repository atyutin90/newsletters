package com.example.newsletters.entity.converters

import com.example.newsletters.entity.enum.QueueType.Companion.queueTypeOf
import com.example.newsletters.entity.enum.QueueType
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class QueueTypeConverter : AttributeConverter<QueueType?, String?> {

    override fun convertToDatabaseColumn(queueType: QueueType?): String? = queueType?.name

    override fun convertToEntityAttribute(value: String?): QueueType? = queueTypeOf(value)
}

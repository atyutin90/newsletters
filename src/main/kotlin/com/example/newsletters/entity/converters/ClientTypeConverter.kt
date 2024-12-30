package com.example.newsletters.entity.converters

import com.example.newsletters.entity.enum.ClientType
import com.example.newsletters.entity.enum.ClientType.Companion.clientTypeOf
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class ClientTypeConverter : AttributeConverter<ClientType?, String?> {

    override fun convertToDatabaseColumn(clientType: ClientType?): String? = clientType?.name

    override fun convertToEntityAttribute(value: String?): ClientType? = clientTypeOf(value)
}

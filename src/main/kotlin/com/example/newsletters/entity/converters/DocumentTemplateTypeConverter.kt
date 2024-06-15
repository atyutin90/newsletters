package com.example.newsletters.entity.converters

import com.example.newsletters.entity.enum.DocumentTemplateType
import com.example.newsletters.entity.enum.DocumentTemplateType.Companion.documentTemplateTypeOf
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class DocumentTemplateTypeConverter : AttributeConverter<DocumentTemplateType?, String?> {

    override fun convertToDatabaseColumn(documentTemplateType: DocumentTemplateType?): String? = documentTemplateType?.name

    override fun convertToEntityAttribute(value: String?): DocumentTemplateType? = documentTemplateTypeOf(value)
}

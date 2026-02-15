package com.example.newsletters.dto.annotations

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.beans.BeanWrapperImpl

class NotEmptyDependentFieldValidator : ConstraintValidator<NotEmptyDependentField, Any> {
    var mainField: String? = null
    var dependentField: String? = null
    var expectedValues: MutableList<String>? = null
    var message: String? = null

    override fun initialize(annotation: NotEmptyDependentField) {
        this.mainField = annotation.mainField
        this.dependentField = annotation.dependentField
        this.expectedValues = annotation.expectedValues.toMutableList()
        this.message = annotation.message
    }

    override fun isValid(o: Any, context: ConstraintValidatorContext): Boolean {
        var result = true
        val mainFieldValue: Any? = BeanWrapperImpl(o).getPropertyValue(requireNotNull(mainField))
        val dependentFieldValue: Any? = BeanWrapperImpl(o).getPropertyValue(requireNotNull(dependentField))

        if (mainFieldValue != null && expectedValues?.contains(mainFieldValue.toString()) == true) {
            context
                .buildConstraintViolationWithTemplate(message)
                .addPropertyNode(dependentField).addConstraintViolation()
            result = dependentFieldValue != null && !dependentFieldValue.toString().isEmpty()
        }
        return result
    }
}

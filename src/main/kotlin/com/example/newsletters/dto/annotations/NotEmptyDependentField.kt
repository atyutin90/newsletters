package com.example.newsletters.dto.annotations

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Аннотация для проверки заполненности зависимого поля при определенных значения основного поля.
 */
@Constraint(validatedBy = [NotEmptyDependentFieldValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@JvmRepeatable(value = NotEmptyDependentFields::class)
annotation class NotEmptyDependentField(
    val message: String = "{jakarta.validation.constraints.NotBlank.message}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val mainField: String,
    val dependentField: String,
    val expectedValues: Array<String> = []
)

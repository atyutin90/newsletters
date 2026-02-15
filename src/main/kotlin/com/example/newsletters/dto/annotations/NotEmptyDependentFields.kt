package com.example.newsletters.dto.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class NotEmptyDependentFields(
    vararg val value: NotEmptyDependentField
)

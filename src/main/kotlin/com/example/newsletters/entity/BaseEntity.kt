package com.example.newsletters.entity

import java.time.ZonedDateTime

interface BaseEntity {
    val id: Long?
    var createdAt: ZonedDateTime?
    var updatedAt: ZonedDateTime?
}

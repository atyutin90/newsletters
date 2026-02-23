package com.example.newsletters.dto.model

import com.example.newsletters.dto.annotations.NotEmptyDependentField
import jakarta.validation.constraints.NotEmpty

@NotEmptyDependentField(
    mainField = "type",
    dependentField = "creditorId",
    expectedValues = ["CREDITOR"]
)

@NotEmptyDependentField(
    mainField = "type",
    dependentField = "name",
    expectedValues = ["PARTICIPANT"]
)
data class DebtorMeetingParticipantDto(
    val id: Long? = null,
    val debtorMeetingId: Long,
    @field:NotEmpty
    val type: String? = null,
    val withoutRight: Boolean = false,
    val creditorId: Long? = null,
    var name: String? = null,
    val address: String? = null,
)

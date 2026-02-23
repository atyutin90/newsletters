package com.example.newsletters.dto.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import java.io.Serializable

@JsonInclude(NON_NULL)
class DocumentInfo(
    var creditorId: Long? = null,
    var creditorIds: List<Long>? = null,
    var requestId: Long? = null,
) : Serializable

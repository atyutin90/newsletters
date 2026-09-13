package com.example.newsletters.dto.filter.specification

import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.entity.RequestDestination
import org.springframework.data.jpa.domain.Specification

class RequestDestinationSpecification {

    companion object {
        fun requestDestinationFilterSpecification(filter: PageFilter): Specification<RequestDestination> = run {
            val search = filter.search?.trim()?.lowercase()
            if (search.isNullOrEmpty()) {
                Specification.unrestricted()
            } else {
                Specification { root, _, cb ->
                    cb.like(cb.lower(root.get("name")), "%$search%")
                }
            }
        }
    }
}

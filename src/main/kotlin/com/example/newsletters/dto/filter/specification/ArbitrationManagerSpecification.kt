package com.example.newsletters.dto.filter.specification

import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.entity.ArbitrationManager
import org.springframework.data.jpa.domain.Specification

class ArbitrationManagerSpecification {

    companion object {
        fun userFilterSpecification(filter: PageFilter): Specification<ArbitrationManager> = run {
            val search = filter.search?.trim()?.lowercase()?.trim()
            if (search.isNullOrEmpty()) {
                Specification.unrestricted()
            } else {
                Specification { root, _, cb ->
                    cb.like(root.get("fullName"), "%$search%")
                }
            }
        }
    }
}
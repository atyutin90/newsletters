package com.example.newsletters.dto.filter.specification

import com.example.newsletters.dto.model.PageFilter
import com.example.newsletters.entity.Question
import org.springframework.data.jpa.domain.Specification

class QuestionSpecification {

    companion object {
        fun questionFilterSpecification(filter: PageFilter): Specification<Question> = run {
            val search = filter.search?.trim()?.lowercase()
            if (search.isNullOrEmpty()) {
                Specification.unrestricted()
            } else {
                Specification { root, _, cb ->
                    cb.like(cb.lower(root.get("value")), "%$search%")
                }
            }
        }
    }
}

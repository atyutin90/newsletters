package com.example.newsletters.repository

import com.example.newsletters.entity.RequestDestination
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Transactional
@Repository
interface RequestDestinationRepository : JpaRepository<RequestDestination, Long>,
    JpaSpecificationExecutor<RequestDestination> {

    override fun findAll(spec: Specification<RequestDestination>, pageable: Pageable): Page<RequestDestination>

    fun findAllByOrderByNameAsc(): List<RequestDestination>
}

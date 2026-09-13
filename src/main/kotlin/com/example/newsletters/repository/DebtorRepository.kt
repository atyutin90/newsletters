package com.example.newsletters.repository

import com.example.newsletters.entity.Debtor
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
@Transactional
interface DebtorRepository : JpaRepository<Debtor, Long>, JpaSpecificationExecutor<Debtor> {

    override fun findAll(spec: Specification<Debtor>, pageable: Pageable): Page<Debtor>

    fun findByArbitrationManagerId(arbitrationManagerId: Long): List<Debtor>
}

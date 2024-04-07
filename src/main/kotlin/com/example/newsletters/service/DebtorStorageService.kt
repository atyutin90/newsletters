package com.example.newsletters.service

import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.LocationDto
import com.example.newsletters.entity.Debtor
import com.example.newsletters.repository.DebtorRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DebtorStorageService(private val debtorRepository: DebtorRepository) {
    fun getAll(): List<DebtorDto> = debtorRepository.findAll().map { it.debtorDTO }
    fun getAll(paging: Pageable) = debtorRepository.findAll(paging).map { it.debtorDTO }
    fun getByName(name: String) = debtorRepository.findByNameContainingIgnoreCase(name).map { it.debtorDTO }
    fun getByName(name: String, paging: Pageable) = debtorRepository.findByNameContainingIgnoreCase(name, paging).map { it.debtorDTO }
    fun getById(id: Int) = debtorRepository.findById(id).map { it.debtorDTO }.orElse(null)
    fun getByIds(ids: List<Int>) = debtorRepository.findAllById(ids).map { it.debtorDTO }
    fun delete(id: Int) = debtorRepository.deleteById(id)
    fun create(debtor: DebtorDto) = debtorRepository.save(debtor.debtor)
    fun update(debtor: DebtorDto) = debtorRepository.save(debtor.debtor)
}

val Debtor.debtorDTO get() = DebtorDto(id = id, name = name)
val DebtorDto.debtor get() = Debtor(id = id, name = name)

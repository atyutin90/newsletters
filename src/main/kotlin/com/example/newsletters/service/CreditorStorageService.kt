package com.example.newsletters.service

import com.example.newsletters.dto.CreditorDto
import com.example.newsletters.dto.TemplateDto
import com.example.newsletters.entity.Creditor
import com.example.newsletters.repository.CreditorRepository
import org.springframework.stereotype.Service

@Service
class CreditorStorageService(private val creditorRepository: CreditorRepository) {
    fun getAll(): List<CreditorDto> = creditorRepository.findAll().map { it.creditorDTO }

    fun getByName(name: String) = creditorRepository.findByNameContainingIgnoreCase(name).map { it.creditorDTO }

    fun getById(id: Int) = creditorRepository.findById(id).map { it.creditorDTO }.orElse(null)

    fun getByIds(ids: List<Int>) = creditorRepository.findAllById(ids).map { it.creditorDTO }

    fun delete(id: Int) = creditorRepository.deleteById(id)

    fun create(creditor: CreditorDto) = creditorRepository.save(creditor.creditor)

    fun update(creditor: CreditorDto) = creditorRepository.save(creditor.creditor)
}

val Creditor.creditorDTO get() = CreditorDto(
    id = id,
    name = name,
    voiceNumbers = voiceNumbers
)

val CreditorDto.creditor get() = Creditor(
    id = id,
    name = name,
    voiceNumbers = voiceNumbers
)

package com.example.newsletters.service

import com.example.newsletters.dto.FileDto
import com.example.newsletters.entity.FileDB
import com.example.newsletters.repository.FileDBRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile


@Service
class FileStorageService(val fileDBRepository: FileDBRepository) {
    fun store(name: String, file: FileDto): FileDB = fileDBRepository.save(FileDB(name, file.type, file.data))
    fun getById(id: Int): FileDto? = fileDBRepository.findById(id).get().fileDto
    fun getByName(name: String) = fileDBRepository.findByNameContainingIgnoreCase(name).map { it.fileDto }
    fun getByNameOrderByCreatedAtDesc(name: String, paging: Pageable): Page<FileDto> = fileDBRepository.findByNameContainingIgnoreCaseOrderByCreatedAtDesc(name, paging).map { it.fileDto }
    fun getAll(): List<FileDto> = fileDBRepository.findAll().map { it.fileDto }
    fun getAllOrderByCreatedAtDesc(paging: Pageable): Page<FileDto> = fileDBRepository.findByOrderByCreatedAtDesc(paging).map { it.fileDto }
    fun delete(id: Int) = fileDBRepository.deleteById(id)
}

val FileDB.fileDto: FileDto
    get() = FileDto(
        id = this.id,
        name = this.name,
        createdAt = this.createdAt,
        fileName = this.name,
        type = this.type,
        data = this.data
    )

package com.example.newsletters.service

import com.example.newsletters.dto.DebtorDto
import com.example.newsletters.dto.LocationDto
import com.example.newsletters.entity.Debtor
import com.example.newsletters.entity.Location
import com.example.newsletters.repository.DebtorRepository
import com.example.newsletters.repository.LocationRepository
import org.springframework.stereotype.Service

@Service
class LocationStorageService(private val locationRepository: LocationRepository) {
    fun getAll(): List<LocationDto> = locationRepository.findAll().map { it.locationDTO }
    fun getByName(name: String) = locationRepository.findByNameContainingIgnoreCase(name).map { it.locationDTO }
    fun getById(id: Int) = locationRepository.findById(id).map { it.locationDTO }.orElse(null)
    fun getByIds(ids: List<Int>) = locationRepository.findAllById(ids).map { it.locationDTO }
    fun delete(id: Int) = locationRepository.deleteById(id)
    fun create(location: LocationDto) = locationRepository.save(location.location)
    fun update(location: LocationDto) = locationRepository.save(location.location)
}

val Location.locationDTO get() = LocationDto(id = id, name = name)

val LocationDto.location get() = Location(id = id, name = name)

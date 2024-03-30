package com.example.newsletters.controller

import com.example.newsletters.dto.LocationDto
import com.example.newsletters.service.LocationStorageService
import org.springframework.context.MessageSource
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
@RequestMapping("/location")
class LocationController(val locationStorageService: LocationStorageService, val messageSource: MessageSource) :
    AbstractController {

    @GetMapping("/all")
    fun getAll(@Param(KEYWORD) keyword: String?, model: Model): String = run {
        try {
            val locations: MutableList<LocationDto> = mutableListOf()
            if (keyword == null) {
                locationStorageService.getAll().forEach(locations::add)
            } else {
                locationStorageService.getByName(keyword).forEach(locations::add)
                model.addAttribute(KEYWORD, keyword)
            }
            model.addAttribute(LOCATIONS, locations)
        } catch (e: Exception) {
            model.addAttribute(MESSAGE, e.message)
        }
        LOCATIONS
    }

    @GetMapping("/new")
    fun add(model: Model): String = run {
        model.addAttribute(LOCATION, LocationDto())
        model.addAttribute(PAGE_TITLE, messageSource.getMessage("create-location", arrayOf(), Locale.getDefault()))
        "location_form"
    }

    @PostMapping("/save")
    fun save(location: LocationDto, redirectAttributes: RedirectAttributes): String = run {
        try {
            if (location.id != null) locationStorageService.update(location)
            else locationStorageService.create(location)
            redirectAttributes.addFlashAttribute(
                MESSAGE,
                messageSource.getMessage("record-successfully-created", arrayOf(), Locale.getDefault())
            )
        } catch (e: Exception) {
            redirectAttributes.addAttribute(MESSAGE, e.message)
        }
        "redirect:/location/all"
    }

    @GetMapping("/{id}")
    fun edit(@PathVariable(ID) id: Int, model: Model, redirectAttributes: RedirectAttributes): String =
        try {
            val location: LocationDto = locationStorageService.getById(id)
            model.addAttribute(LOCATION, location)
            model.addAttribute(
                PAGE_TITLE,
                messageSource.getMessage("update-location", arrayOf(id), Locale.getDefault())
            )
            "location_form"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
            "redirect:/location/all"
        }

    @GetMapping("/delete/{id}")
    fun delete(@PathVariable(ID) id: Int, model: Model, redirectAttributes: RedirectAttributes): String = run {
        try {
            locationStorageService.delete(id)
            redirectAttributes.addFlashAttribute(
                MESSAGE,
                messageSource.getMessage("record-successfully-deleted", arrayOf(id), Locale.getDefault())
            )
        } catch (e: java.lang.Exception) {
            redirectAttributes.addFlashAttribute(MESSAGE, e.message)
        }
        "redirect:/location/all"
    }
}

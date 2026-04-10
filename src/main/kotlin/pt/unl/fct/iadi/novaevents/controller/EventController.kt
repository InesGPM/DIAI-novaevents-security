package pt.unl.fct.iadi.novaevents.controller

import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import pt.unl.fct.iadi.novaevents.controller.dto.EventFormDto
import pt.unl.fct.iadi.novaevents.service.ClubService
import pt.unl.fct.iadi.novaevents.service.EventService

@Controller
class EventController(val eventService: EventService, val clubService: ClubService) {

    @GetMapping("/events")
    fun listEvents(
        @RequestParam(required = false) typeId: Long?,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) clubId: Long?,
        model: Model
    ): String {
        val resolvedTypeId = typeId ?: type?.let {
            eventService.getAllTypes().find { t -> t.name == it }?.id
        }
        val clubs = clubService.getAll()
        val events = eventService.getFiltered(clubId, resolvedTypeId)
        val eventTypes = eventService.getAllTypes()
        model.addAttribute("events", events)
        model.addAttribute("clubs", clubs)
        model.addAttribute("eventTypes", eventTypes)
        model.addAttribute("clubMap", clubs.associate { it.id to it.name })
        return "events/list"
    }

    @GetMapping("/clubs/{clubId}/events/{id}")
    fun eventDetail(@PathVariable clubId: Long, @PathVariable id: Long, model: Model): String {
        model.addAttribute("event", eventService.getById(id))
        model.addAttribute("club", clubService.getById(clubId))
        return "events/detail"
    }

    @GetMapping("/clubs/{clubId}/events/new")
    fun showCreateForm(@PathVariable clubId: Long, model: Model): String {
        model.addAttribute("club", clubService.getById(clubId))
        model.addAttribute("form", EventFormDto())
        model.addAttribute("eventTypes", eventService.getAllTypes())
        return "events/new"
    }

    @PostMapping("/clubs/{clubId}/events")
    fun createEvent(
        @PathVariable clubId: Long,
        @Valid @ModelAttribute("form") form: EventFormDto,
        bindingResult: BindingResult,
        model: Model
    ): String {

        val resolvedTypeId = form.typeId
            ?: form.type?.let { typeName ->
                eventService.getAllTypes().find { it.name == typeName }?.id
            }

        if (resolvedTypeId == null) {
            bindingResult.rejectValue("typeId", "required", "Event type is required")
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("club", clubService.getById(clubId))
            model.addAttribute("eventTypes", eventService.getAllTypes())
            return "events/new"
        }

        try {
            val event = eventService.create(
                clubId = clubId,
                name = form.name,
                date = form.date!!,
                location = form.location,
                typeId = resolvedTypeId!!,
                description = form.description
            )
            return "redirect:/clubs/$clubId/events/${event.id}"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("name", "duplicate", e.message ?: "An event with this name already exists")
            model.addAttribute("club", clubService.getById(clubId))
            model.addAttribute("eventTypes", eventService.getAllTypes())
            return "events/new"
        }
    }

    @GetMapping("/clubs/{clubId}/events/{id}/edit")
    fun showEditForm(@PathVariable clubId: Long, @PathVariable id: Long, model: Model): String {
        val event = eventService.getById(id)
        val form = EventFormDto(
            name = event.name,
            date = event.date,
            location = event.location,
            typeId = event.type?.id,
            description = event.description
        )
        model.addAttribute("club", clubService.getById(clubId))  // objeto Club completo
        model.addAttribute("event", event)  // objeto Event completo
        model.addAttribute("form", form)
        model.addAttribute("eventTypes", eventService.getAllTypes())
        return "events/edit"
    }

    @PostMapping("/clubs/{clubId}/events/{id}", params = ["_method=PUT"])
    fun updateEvent(
        @PathVariable clubId: Long,
        @PathVariable id: Long,
        @Valid @ModelAttribute("form") form: EventFormDto,
        bindingResult: BindingResult,
        model: Model
    ): String {

        val resolvedTypeId = form.typeId
            ?: form.type?.let { typeName ->
                eventService.getAllTypes().find { it.name == typeName }?.id
            }

        if (resolvedTypeId == null) {
            bindingResult.rejectValue("typeId", "required", "Event type is required")
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("club", clubService.getById(clubId))
            model.addAttribute("event", eventService.getById(id))
            model.addAttribute("eventTypes", eventService.getAllTypes())
            return "events/edit"
        }

        try {
            val event = eventService.update(
                id = id,
                name = form.name,
                date = form.date!!,
                location = form.location,
                typeId = resolvedTypeId!!,
                description = form.description
            )
            return "redirect:/clubs/$clubId/events/${event.id}"
        } catch (e: IllegalArgumentException) {
            bindingResult.rejectValue("name", "duplicate", e.message ?: "An event with this name already exists")
            model.addAttribute("club", clubService.getById(clubId))
            model.addAttribute("event", eventService.getById(id))
            model.addAttribute("eventTypes", eventService.getAllTypes())
            return "events/edit"
        }
    }

    @GetMapping("/clubs/{clubId}/events/{id}/delete")
    fun showDeleteConfirm(@PathVariable clubId: Long, @PathVariable id: Long, model: Model): String {
        model.addAttribute("club", clubService.getById(clubId))
        model.addAttribute("event", eventService.getById(id))
        return "events/delete"
    }

    @PostMapping("/clubs/{clubId}/events/{id}", params = ["_method=DELETE"])
    fun deleteEvent(@PathVariable clubId: Long, @PathVariable id: Long): String {
        eventService.delete(id)
        return "redirect:/clubs/$clubId"
    }
    @PutMapping("/clubs/{clubId}/events/{id}")
    fun updateEventPut(@PathVariable clubId: Long, @PathVariable id: Long,
                       @Valid @ModelAttribute("form") form: EventFormDto, bindingResult: BindingResult, model: Model): String {
        return updateEvent(clubId, id, form, bindingResult, model)
    }

    @DeleteMapping("/clubs/{clubId}/events/{id}")
    fun deleteEventDelete(@PathVariable clubId: Long, @PathVariable id: Long): String {
        return deleteEvent(clubId, id)
    }



}
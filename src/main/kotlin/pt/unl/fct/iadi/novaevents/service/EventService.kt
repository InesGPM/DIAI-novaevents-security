package pt.unl.fct.iadi.novaevents.service

import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository
import java.time.LocalDate
import java.util.*

@Service
class EventService (val clubRepository: ClubRepository, val eventRepository: EventRepository,val eventTypeRepository: EventTypeRepository) {

    fun getAll(): List<Event> = eventRepository.findAllWithClubAndType()

    fun getById(id: Long): Event=
        eventRepository.findById(id).orElseThrow  { NoSuchElementException("Event not found: $id") }

    fun getByClubId(clubId: Long): List<Event> {
        val club = clubRepository.findById(clubId).orElseThrow { NoSuchElementException("Club not found: $clubId") }
        return eventRepository.findByClub(club)
    }
    fun create(clubId: Long, name: String, date: LocalDate,
               location: String?, typeId: Long, description: String?): Event {
        if (eventRepository.existsByNameIgnoreCase(name)) {
            throw IllegalArgumentException("An event with this name already exists")
        }
        val club = clubRepository.findById(clubId)
            .orElseThrow { NoSuchElementException("Club not found: $clubId") }
        val eventType = eventTypeRepository.findById(typeId)
            .orElseThrow { NoSuchElementException("EventType not found") }
        return eventRepository.save(Event(club = club, name = name, date = date,
            location = location, type = eventType, description = description))
    }

    fun update(id: Long, name: String, date: LocalDate,
               location: String?, typeId: Long, description: String?): Event {
        val event = eventRepository.findById(id)
            .orElseThrow { NoSuchElementException("Event not found: $id") }
        if (eventRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw IllegalArgumentException("An event with this name already exists")
        }
        val eventType = eventTypeRepository.findById(typeId)
            .orElseThrow { NoSuchElementException("EventType not found") }
        event.name = name
        event.date = date
        event.location = location
        event.type = eventType
        event.description = description
        return eventRepository.save(event)
    }


    fun delete(id: Long) {
        val event = eventRepository.findById(id)
            .orElseThrow { NoSuchElementException("Event not found: $id") }

        eventRepository.delete(event)
    }

    fun getFiltered(clubId: Long?, typeId: Long?): List<Event> {
        val club = clubId?.let { clubRepository.findById(it).orElse(null) }
        val type = typeId?.let { eventTypeRepository.findById(it).orElse(null) }
        return when {
            club != null && type != null -> eventRepository.findByClubAndTypeWithFetch(club, type)
            club != null -> eventRepository.findByClubWithType(club)
            type != null -> eventRepository.findByTypeWithClub(type)
            else -> eventRepository.findAllWithClubAndType()
        }
    }

    fun getAllTypes(): List<EventType> = eventTypeRepository.findAll()
}

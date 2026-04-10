package pt.unl.fct.iadi.novaevents.security

import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.repository.EventRepository

@Component("eventSecurity")
class EventSecurity(
    private val eventRepository: EventRepository
) {

    fun isOwner(eventId: Long, username: String): Boolean {
        val event = eventRepository.findById(eventId).orElse(null) ?: return false
        return event.owner?.username == username
    }

    fun isOwnerOrAdmin(eventId: Long, username: String, isAdmin: Boolean): Boolean {
        if (isAdmin) return true
        return isOwner(eventId, username)
    }
}
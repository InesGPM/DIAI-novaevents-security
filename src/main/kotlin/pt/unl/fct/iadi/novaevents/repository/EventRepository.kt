package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType

interface EventRepository : JpaRepository<Event, Long> {
    fun existsByNameIgnoreCase(name: String): Boolean
    fun existsByNameIgnoreCaseAndIdNot(name: String, id: Long): Boolean
    fun findByClub(club: Club): List<Event>
    fun findByType(type: EventType): List<Event>
    fun findByClubAndType(club: Club, type: EventType): List<Event>
    @Query("SELECT e FROM Event e JOIN FETCH e.club JOIN FETCH e.type")
    fun findAllWithClubAndType(): List<Event>
    @Query("SELECT e FROM Event e JOIN FETCH e.club JOIN FETCH e.type WHERE e.club = :club")
    fun findByClubWithType(club: Club): List<Event>

    @Query("SELECT e FROM Event e JOIN FETCH e.club JOIN FETCH e.type WHERE e.type = :type")
    fun findByTypeWithClub(type: EventType): List<Event>

    @Query("SELECT e FROM Event e JOIN FETCH e.club JOIN FETCH e.type WHERE e.club = :club AND e.type = :type")
    fun findByClubAndTypeWithFetch(club: Club, type: EventType): List<Event>
}

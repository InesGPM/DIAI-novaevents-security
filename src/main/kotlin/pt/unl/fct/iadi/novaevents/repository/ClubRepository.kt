package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.unl.fct.iadi.novaevents.model.Club
import org.springframework.data.jpa.repository.Query

interface ClubRepository : JpaRepository<Club, Long> {
    @Query("SELECT c.id, COUNT(e) FROM Club c LEFT JOIN Event e ON e.club = c GROUP BY c.id")
    fun countEventsByClub(): List<Array<Any>>
}
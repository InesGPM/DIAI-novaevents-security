package pt.unl.fct.iadi.novaevents.service

import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategory
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.repository.ClubRepository

@Service
class ClubService(val clubRepository: ClubRepository) {

    fun getAll(): List<Club> = clubRepository.findAll()

    fun getById(id: Long): Club =
        clubRepository.findById(id).orElseThrow { NoSuchElementException("Club not found: $id") }

    fun getEventCountByClub(): Map<Long, Long> =
        clubRepository.countEventsByClub().associate {
            (it[0] as Long) to (it[1] as Long)
        }
}
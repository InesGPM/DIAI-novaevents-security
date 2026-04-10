package pt.unl.fct.iadi.novaevents

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategory
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository
import java.time.LocalDate

@Component
class DataInitializer(
    private val eventTypeRepository: EventTypeRepository,
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        println("DataInitializer running...")

        // Verificação principal: se já existem EventTypes, assumimos que tudo foi seeded
        if (eventTypeRepository.count() > 0) {
            println("Database already seeded. Skipping initialization.")
            return
        }

        println("Seeding database...")

        // ==================== 1. EventTypes ====================
        val workshop = eventTypeRepository.save(EventType(name = "WORKSHOP"))
        val competition = eventTypeRepository.save(EventType(name = "COMPETITION"))
        val talk = eventTypeRepository.save(EventType(name = "TALK"))
        val meeting = eventTypeRepository.save(EventType(name = "MEETING"))
        val social = eventTypeRepository.save(EventType(name = "SOCIAL"))
        val other = eventTypeRepository.save(EventType(name = "OTHER"))

        println("→ EventTypes seeded")

        // ==================== 2. Clubs ====================
        val chessClub = clubRepository.save(
            Club(name = "Chess Club",
                description = "From beginner to advanced, our Chess Club welcomes all levels.",
                category = ClubCategory.ACADEMIC)
        )

        val roboticsClub = clubRepository.save(
            Club(name = "Robotics Club",
                description = "The Robotics Club is the place to turn ideas into machines.",
                category = ClubCategory.TECHNOLOGY)
        )

        val photographyClub = clubRepository.save(
            Club(name = "Photography Club",
                description = "We are a community of visual storytellers passionate about photography.",
                category = ClubCategory.ARTS)
        )

        val hikingClub = clubRepository.save(
            Club(name = "Hiking & Outdoors Club",
                description = "The Hiking & Outdoors Club organises regular excursions into nature.",
                category = ClubCategory.SPORTS)
        )

        val filmClub = clubRepository.save(
            Club(name = "Film Society",
                description = "The Film Society screens a curated selection of films from around the world.",
                category = ClubCategory.CULTURAL)
        )

        println("→ Clubs seeded")

        // ==================== 3. Events ====================
        eventRepository.saveAll(
            listOf(
                Event(club = chessClub, name = "Beginner's Chess Workshop", date = LocalDate.of(2026, 3, 10),
                    location = "Room A101", type = workshop, description = "Workshop para iniciantes"),
                Event(club = chessClub, name = "Spring Chess Tournament", date = LocalDate.of(2026, 4, 5),
                    location = "Main Hall", type = competition, description = "Torneio de primavera"),
                Event(club = chessClub, name = "Advanced Openings Talk", date = LocalDate.of(2026, 5, 20),
                    location = "Room A101", type = talk, description = "Aberturas avançadas"),

                Event(club = roboticsClub, name = "Arduino Intro Workshop", date = LocalDate.of(2026, 3, 15),
                    location = "Engineering Lab 2", type = workshop, description = "Introdução ao Arduino"),
                Event(club = roboticsClub, name = "RoboCup Preparation Meeting", date = LocalDate.of(2026, 3, 28),
                    location = "Engineering Lab 1", type = meeting, description = "Preparação para RoboCup"),
                Event(club = roboticsClub, name = "Sensor Integration Talk", date = LocalDate.of(2026, 4, 22),
                    location = "Auditorium B", type = talk, description = "Integração de sensores"),
                Event(club = roboticsClub, name = "Regional Robotics Competition", date = LocalDate.of(2026, 6, 1),
                    location = "Sports Hall", type = competition, description = "Competição regional"),

                Event(club = photographyClub, name = "Night Photography Walk", date = LocalDate.of(2026, 3, 20),
                    location = "Campus Gardens", type = social, description = "Passeio fotográfico noturno"),
                Event(club = photographyClub, name = "Lightroom Workshop", date = LocalDate.of(2026, 4, 10),
                    location = "Lab 3", type = workshop, description = "Edição com Lightroom"),

                Event(club = hikingClub, name = "Serra da Arrábida Hike", date = LocalDate.of(2026, 3, 22),
                    location = "Arrábida", type = social, description = "Caminhada na Arrábida"),
                Event(club = hikingClub, name = "Trail Safety Talk", date = LocalDate.of(2026, 4, 15),
                    location = "Room B202", type = talk, description = "Segurança em trilhos"),

                Event(club = filmClub, name = "Kubrick Retrospective", date = LocalDate.of(2026, 3, 25),
                    location = "Auditorium A", type = other, description = "Retrospetiva de Kubrick"),
                Event(club = filmClub, name = "Scriptwriting Workshop", date = LocalDate.of(2026, 4, 18),
                    location = "Room C101", type = workshop, description = "Escrita de guiões")
            )
        )

        println("→ Events seeded")
        println("Database seeding completed successfully!")
    }
}
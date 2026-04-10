package pt.unl.fct.iadi.novaevents

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategory
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.model.RoleName
import pt.unl.fct.iadi.novaevents.model.User
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import pt.unl.fct.iadi.novaevents.repository.EventTypeRepository
import pt.unl.fct.iadi.novaevents.repository.UserRepository
import java.time.LocalDate

@Component
class DataInitializer(
    private val eventTypeRepository: EventTypeRepository,
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        println("DataInitializer running...")

        seedUsers()

        if (eventTypeRepository.count() > 0) {
            println("Database already seeded. Skipping initialization.")
            return
        }

        println("Seeding database...")

        val alice = userRepository.findByUsername("alice").orElseThrow()
        val bob = userRepository.findByUsername("bob").orElseThrow()
        val charlie = userRepository.findByUsername("charlie").orElseThrow()

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
            Club(
                name = "Chess Club",
                description = "From beginner to advanced, our Chess Club welcomes all levels.",
                category = ClubCategory.ACADEMIC
            )
        )

        val roboticsClub = clubRepository.save(
            Club(
                name = "Robotics Club",
                description = "The Robotics Club is the place to turn ideas into machines.",
                category = ClubCategory.TECHNOLOGY
            )
        )

        val photographyClub = clubRepository.save(
            Club(
                name = "Photography Club",
                description = "We are a community of visual storytellers passionate about photography.",
                category = ClubCategory.ARTS
            )
        )

        val hikingClub = clubRepository.save(
            Club(
                name = "Hiking & Outdoors Club",
                description = "The Hiking & Outdoors Club organises regular excursions into nature.",
                category = ClubCategory.SPORTS
            )
        )

        val filmClub = clubRepository.save(
            Club(
                name = "Film Society",
                description = "The Film Society screens a curated selection of films from around the world.",
                category = ClubCategory.CULTURAL
            )
        )

        println("→ Clubs seeded")

        // ==================== 3. Events ====================
        eventRepository.saveAll(
            listOf(
                Event(
                    club = chessClub,
                    name = "Beginner's Chess Workshop",
                    date = LocalDate.of(2026, 3, 10),
                    location = "Room A101",
                    type = workshop,
                    description = "Workshop para iniciantes",
                    owner = alice
                ),
                Event(
                    club = chessClub,
                    name = "Spring Chess Tournament",
                    date = LocalDate.of(2026, 4, 5),
                    location = "Main Hall",
                    type = competition,
                    description = "Torneio de primavera",
                    owner = alice
                ),
                Event(
                    club = chessClub,
                    name = "Advanced Openings Talk",
                    date = LocalDate.of(2026, 5, 20),
                    location = "Room A101",
                    type = talk,
                    description = "Aberturas avançadas",
                    owner = alice
                ),

                Event(
                    club = roboticsClub,
                    name = "Arduino Intro Workshop",
                    date = LocalDate.of(2026, 3, 15),
                    location = "Engineering Lab 2",
                    type = workshop,
                    description = "Introdução ao Arduino",
                    owner = bob
                ),
                Event(
                    club = roboticsClub,
                    name = "RoboCup Preparation Meeting",
                    date = LocalDate.of(2026, 3, 28),
                    location = "Engineering Lab 1",
                    type = meeting,
                    description = "Preparação para RoboCup",
                    owner = bob
                ),
                Event(
                    club = roboticsClub,
                    name = "Sensor Integration Talk",
                    date = LocalDate.of(2026, 4, 22),
                    location = "Auditorium B",
                    type = talk,
                    description = "Integração de sensores",
                    owner = bob
                ),
                Event(
                    club = roboticsClub,
                    name = "Regional Robotics Competition",
                    date = LocalDate.of(2026, 6, 1),
                    location = "Sports Hall",
                    type = competition,
                    description = "Competição regional",
                    owner = bob
                ),

                Event(
                    club = photographyClub,
                    name = "Night Photography Walk",
                    date = LocalDate.of(2026, 3, 20),
                    location = "Campus Gardens",
                    type = social,
                    description = "Passeio fotográfico noturno",
                    owner = alice
                ),
                Event(
                    club = photographyClub,
                    name = "Lightroom Workshop",
                    date = LocalDate.of(2026, 4, 10),
                    location = "Lab 3",
                    type = workshop,
                    description = "Edição com Lightroom",
                    owner = alice
                ),

                Event(
                    club = hikingClub,
                    name = "Serra da Arrábida Hike",
                    date = LocalDate.of(2026, 3, 22),
                    location = "Arrábida",
                    type = social,
                    description = "Caminhada na Arrábida",
                    owner = charlie
                ),
                Event(
                    club = hikingClub,
                    name = "Trail Safety Talk",
                    date = LocalDate.of(2026, 4, 15),
                    location = "Room B202",
                    type = talk,
                    description = "Segurança em trilhos",
                    owner = charlie
                ),

                Event(
                    club = filmClub,
                    name = "Kubrick Retrospective",
                    date = LocalDate.of(2026, 3, 25),
                    location = "Auditorium A",
                    type = other,
                    description = "Retrospetiva de Kubrick",
                    owner = charlie
                ),
                Event(
                    club = filmClub,
                    name = "Scriptwriting Workshop",
                    date = LocalDate.of(2026, 4, 18),
                    location = "Room C101",
                    type = workshop,
                    description = "Escrita de guiões",
                    owner = charlie
                )
            )
        )

        println("→ Events seeded")
        println("Database seeding completed successfully!")
    }

    private fun seedUsers() {
        if (!userRepository.existsByUsername("alice")) {
            val alice = User(
                username = "alice",
                password = passwordEncoder.encode("password123")
            )
            alice.addRole(RoleName.ROLE_EDITOR)
            userRepository.save(alice)
            println("→ User alice seeded")
        }

        if (!userRepository.existsByUsername("bob")) {
            val bob = User(
                username = "bob",
                password = passwordEncoder.encode("password123")
            )
            bob.addRole(RoleName.ROLE_EDITOR)
            userRepository.save(bob)
            println("→ User bob seeded")
        }

        if (!userRepository.existsByUsername("charlie")) {
            val charlie = User(
                username = "charlie",
                password = passwordEncoder.encode("password123")
            )
            charlie.addRole(RoleName.ROLE_ADMIN)
            userRepository.save(charlie)
            println("→ User charlie seeded")
        }
    }
}
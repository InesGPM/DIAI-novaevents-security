package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Event(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    var club: Club? = null,
    @Column(length = 100)
    var name: String = "",
    var date: LocalDate = LocalDate.now(),
    @Column(length = 100)
    var location: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    var type: EventType? = null,
    var description: String? = null,
)
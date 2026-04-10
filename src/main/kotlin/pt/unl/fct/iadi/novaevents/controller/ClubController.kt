package pt.unl.fct.iadi.novaevents.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pt.unl.fct.iadi.novaevents.service.ClubService

@Controller
@RequestMapping("/clubs")
class ClubController(val clubService: ClubService) {

    @GetMapping("/")
    fun home(): String = "redirect:/clubs"

    @GetMapping
    fun listClubs(model: Model): String {
        model.addAttribute("clubs", clubService.getAll())
        model.addAttribute("eventCounts", clubService.getEventCountByClub())
        return "clubs/list"
    }

    @GetMapping("/{id}")
    fun clubDetail(@PathVariable id: Long, model: Model): String {
        model.addAttribute("club", clubService.getById(id))
        return "clubs/detail"
    }
    @GetMapping("/{id}/events")
    fun clubEvents(@PathVariable id: Long): String {
        return "redirect:/events?clubId=$id"
    }
}
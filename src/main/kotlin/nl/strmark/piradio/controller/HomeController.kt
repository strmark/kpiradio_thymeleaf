package nl.strmark.piradio.controller

import nl.strmark.piradio.service.AlarmService
import nl.strmark.piradio.service.WebRadioService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController(
    private val alarmService: AlarmService,
    private val webRadioService: WebRadioService
) {

    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("alarms", alarmService.findByActive(true))
        model.addAttribute("webRadios", webRadioService.findByIsDefault(true))
        return "home/index"
    }

    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("alarms", alarmService.findByActive(true))
        model.addAttribute("webRadios", webRadioService.findByIsDefault(true))
        return "home/index"
    }
}

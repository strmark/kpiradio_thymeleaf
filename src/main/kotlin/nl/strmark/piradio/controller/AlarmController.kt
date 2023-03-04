package nl.strmark.piradio.controller

import jakarta.validation.Valid
import nl.strmark.piradio.model.AlarmDTO
import nl.strmark.piradio.repos.WebRadioRepository
import nl.strmark.piradio.service.AlarmService
import nl.strmark.piradio.util.WebUtils
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/alarms")
class AlarmController(
    private val alarmService: AlarmService,
    private val webRadioRepository: WebRadioRepository
) {
    companion object {
        private const val alarmsRedirect = "redirect:/alarms"
        private const val alarmsList = "alarm/list"
        private const val alarmsAdd = "alarm/add"
        private const val alarmsEdit = "alarm/edit"
    }

    @ModelAttribute
    fun prepareContext(model: Model) =
        model.addAttribute(
            "alarmWebradioValues",
            webRadioRepository.findAll(Sort.by("id")).associate { it.id to it.name })

    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("alarms", alarmService.findAll())
        return alarmsList
    }

    @GetMapping("/add")
    fun add(@ModelAttribute("alarm") alarmDTO: AlarmDTO) = alarmsAdd

    @PostMapping("/add")
    fun add(
        @ModelAttribute("alarm") @Valid alarmDTO: AlarmDTO,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (bindingResult.hasErrors()) {
            return alarmsAdd
        }
        alarmService.create(alarmDTO)
        redirectAttributes.addFlashAttribute(
            WebUtils.MSG_SUCCESS,
            WebUtils.getMessage("alarm.create.success")
        )
        return alarmsRedirect
    }

    @GetMapping("/edit/{id}")
    fun edit(@PathVariable id: Long, model: Model): String {
        model.addAttribute("alarm", alarmService.get(id))
        return alarmsEdit
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute("alarm") @Valid alarmDTO: AlarmDTO,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (bindingResult.hasErrors()) {
            return "alarm/edit"
        }
        alarmService.update(id, alarmDTO)
        redirectAttributes.addFlashAttribute(
            WebUtils.MSG_SUCCESS,
            WebUtils.getMessage("alarm.update.success")
        )
        return alarmsRedirect
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        alarmService.delete(id)
        redirectAttributes.addFlashAttribute(
            WebUtils.MSG_INFO,
            WebUtils.getMessage("alarm.delete.success")
        )
        return alarmsRedirect
    }
}

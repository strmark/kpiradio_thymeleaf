package nl.strmark.piradio.controller

import jakarta.validation.Valid
import nl.strmark.piradio.model.WebRadioDTO
import nl.strmark.piradio.service.WebRadioService
import nl.strmark.piradio.util.WebUtils
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
@RequestMapping("/webRadios")
class WebRadioController(
    private val webRadioService: WebRadioService
) {
    companion object {
        private const val webRadioRedirect = "redirect:/webRadios"
        private const val webRadioList = "webRadio/list"
        private const val webRadioAdd = "webRadio/add"
        private const val webRadioEdit = "webRadio/edit"
    }

    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("webRadios", webRadioService.findAll())
        return webRadioList
    }

    @GetMapping("/add")
    fun add(@ModelAttribute("webRadio") webRadioDTO: WebRadioDTO) = webRadioAdd

    @PostMapping("/add")
    fun add(
        @ModelAttribute("webRadio") @Valid webRadioDTO: WebRadioDTO,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (bindingResult.hasErrors()) {
            return webRadioAdd
        }
        webRadioService.create(webRadioDTO)
        redirectAttributes.addFlashAttribute(
            WebUtils.MSG_SUCCESS,
            WebUtils.getMessage("webRadio.create.success")
        )
        return webRadioRedirect
    }

    @GetMapping("/edit/{id}")
    fun edit(@PathVariable id: Long, model: Model): String {
        model.addAttribute("webRadio", webRadioService.get(id))
        return webRadioEdit
    }

    @PostMapping("/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        @ModelAttribute("webRadio") @Valid webRadioDTO: WebRadioDTO,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {
        if (bindingResult.hasErrors()) {
            return webRadioEdit
        }
        webRadioService.update(id, webRadioDTO)
        redirectAttributes.addFlashAttribute(
            WebUtils.MSG_SUCCESS,
            WebUtils.getMessage("webRadio.update.success")
        )
        return webRadioRedirect
    }

    @PostMapping("/delete/{id}")
    fun delete(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        val referencedWarning: String? = webRadioService.getReferencedWarning(id)
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning)
        } else {
            webRadioService.delete(id)
            redirectAttributes.addFlashAttribute(
                WebUtils.MSG_INFO,
                WebUtils.getMessage("webRadio.delete.success")
            )
        }
        return webRadioRedirect
    }

}

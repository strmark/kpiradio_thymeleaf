package nl.strmark.piradio.service

import jakarta.transaction.Transactional
import nl.strmark.piradio.domain.WebRadio
import nl.strmark.piradio.model.WebRadioDTO
import nl.strmark.piradio.repos.WebRadioRepository
import nl.strmark.piradio.util.NotFoundException
import nl.strmark.piradio.util.WebUtils
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class WebRadioService(
    private val webRadioRepository: WebRadioRepository
) {

    fun findAll(): List<WebRadioDTO> {
        val webRadios: List<WebRadio> = webRadioRepository.findAll(Sort.by("id"))
        return webRadios.stream()
            .map { webRadio -> mapToDTO(webRadio, WebRadioDTO()) }
            .toList()
    }

    fun findByIsDefault(isDefault: Boolean): List<WebRadioDTO> {
        val webRadios: List<WebRadio> = webRadioRepository.findByIsDefault(true)
        return webRadios.stream()
            .map { webRadio -> mapToDTO(webRadio, WebRadioDTO()) }
            .toList()
    }

    fun `get`(id: Long): WebRadioDTO {
        return webRadioRepository.findById(id)
            .map { webRadio -> mapToDTO(webRadio, WebRadioDTO()) }
            .orElseThrow { NotFoundException() }
    }

    fun create(webRadioDTO: WebRadioDTO): Long {
        val webRadio = WebRadio()
        mapToEntity(webRadioDTO, webRadio)
        return webRadioRepository.save(webRadio).id ?: throw RuntimeException("Save WebRadio did not return Id")
    }

    fun update(id: Long, webRadioDTO: WebRadioDTO) {
        val webRadio: WebRadio = webRadioRepository.findById(id)
            .orElseThrow { NotFoundException() }
        mapToEntity(webRadioDTO, webRadio)
        webRadioRepository.save(webRadio)
    }

    fun delete(id: Long) {
        webRadioRepository.deleteById(id)
    }

    fun mapToDTO(webRadio: WebRadio, webRadioDTO: WebRadioDTO): WebRadioDTO {
        webRadioDTO.id = webRadio.id
        webRadioDTO.name = webRadio.name
        webRadioDTO.url = webRadio.url
        webRadioDTO.isDefault = webRadio.isDefault
        return webRadioDTO
    }

    fun mapToEntity(webRadioDTO: WebRadioDTO, webRadio: WebRadio): WebRadio {
        webRadio.name = webRadioDTO.name
        webRadio.url = webRadioDTO.url
        webRadio.isDefault = webRadioDTO.isDefault
        return webRadio
    }

    @Transactional
    fun getReferencedWarning(id: Long): String? {
        val webRadio: WebRadio = webRadioRepository.findById(id)
            .orElseThrow { NotFoundException() }
        if (!webRadio.alarmWebradioAlarms!!.isEmpty()) {
            return WebUtils.getMessage(
                "webRadio.alarm.manyToOne.referenced",
                webRadio.alarmWebradioAlarms!!.iterator().next().id
            )
        }
        return null
    }

}

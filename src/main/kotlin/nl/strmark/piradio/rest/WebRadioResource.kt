package nl.strmark.piradio.rest

import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import nl.strmark.piradio.model.WebRadioDTO
import nl.strmark.piradio.service.WebRadioService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.noContent
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/api/webRadios"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class WebRadioResource(
    private val webRadioService: WebRadioService
) {

    @GetMapping
    fun getAllWebRadios(): ResponseEntity<List<WebRadioDTO>> =
        ok(webRadioService.findAll())

    @GetMapping("/{id}")
    fun getWebRadio(@PathVariable(name = "id") id: Long): ResponseEntity<WebRadioDTO> =
        ok(webRadioService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createWebRadio(@RequestBody @Valid webRadioDTO: WebRadioDTO): ResponseEntity<Long> =
        ResponseEntity(webRadioService.create(webRadioDTO), HttpStatus.CREATED)

    @PutMapping("/{id}")
    fun updateWebRadio(
        @PathVariable(name = "id") id: Long,
        @RequestBody @Valid
        webRadioDTO: WebRadioDTO
    ): ResponseEntity<Void> {
        webRadioService.update(id, webRadioDTO)
        return ok().build()
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteWebRadio(@PathVariable(name = "id") id: Long): ResponseEntity<Void> {
        webRadioService.delete(id)
        return noContent().build()
    }
}

package nl.strmark.piradio.rest

import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import nl.strmark.piradio.model.AlarmDTO
import nl.strmark.piradio.service.AlarmService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
    value = ["/api/alarms"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class AlarmResource(
    private val alarmService: AlarmService
) {

    @GetMapping
    fun getAllAlarms(): ResponseEntity<List<AlarmDTO>> = ResponseEntity.ok(alarmService.findAll())

    @GetMapping("/{id}")
    fun getAlarm(@PathVariable(name = "id") id: Long): ResponseEntity<AlarmDTO> =
        ResponseEntity.ok(alarmService.get(id))

    @PostMapping
    @ApiResponse(responseCode = "201")
    fun createAlarm(@RequestBody @Valid alarmDTO: AlarmDTO): ResponseEntity<Long> =
        ResponseEntity(alarmService.create(alarmDTO), HttpStatus.CREATED)

    @PutMapping("/{id}")
    fun updateAlarm(@PathVariable(name = "id") id: Long, @RequestBody @Valid alarmDTO: AlarmDTO):
            ResponseEntity<Void> {
        alarmService.update(id, alarmDTO)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    fun deleteAlarm(@PathVariable(name = "id") id: Long): ResponseEntity<Void> {
        alarmService.delete(id)
        return ResponseEntity.noContent().build()
    }

}

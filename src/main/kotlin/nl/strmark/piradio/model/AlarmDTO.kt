package nl.strmark.piradio.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalTime

class AlarmDTO {

    var id: Long? = null

    @NotNull
    @Size(max = 75)
    var name: String? = null

    var monday: Boolean? = null

    var tuesday: Boolean? = null

    var wednesday: Boolean? = null

    var thursday: Boolean? = null

    var friday: Boolean? = null

    var saturday: Boolean? = null

    var sunday: Boolean? = null

    @NotNull
    @Schema(
        type = "string",
        example = "18:30"
    )
    var startTime: LocalTime? = null

    @NotNull
    var autoStopMinutes: Int? = null

    var active: Boolean? = null

    @NotNull
    var alarmWebradio: Long? = null
}

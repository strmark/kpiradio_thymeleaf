package nl.strmark.piradio.model

import jakarta.validation.constraints.Size

class WebRadioDTO {

    var id: Long? = null

    @Size(max = 75)
    var name: String? = null

    @Size(max = 255)
    var url: String? = null

    var isDefault: Boolean? = null

}

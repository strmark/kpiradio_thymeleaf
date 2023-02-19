package nl.strmark.piradio.repos

import nl.strmark.piradio.domain.WebRadio
import org.springframework.data.jpa.repository.JpaRepository

interface WebRadioRepository : JpaRepository<WebRadio, Long> {
    fun findByIsDefault(isDefault: Boolean): List<WebRadio>
}

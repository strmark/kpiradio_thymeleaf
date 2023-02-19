package nl.strmark.piradio.repos

import nl.strmark.piradio.domain.Alarm
import org.springframework.data.jpa.repository.JpaRepository

interface AlarmRepository : JpaRepository<Alarm, Long> {
    fun findByActive(active: Boolean): List<Alarm>
}

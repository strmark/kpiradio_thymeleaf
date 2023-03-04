package nl.strmark.piradio.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.SEQUENCE
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import java.time.LocalTime

@Entity
class Alarm {

    @Id
    @Column(
        nullable = false,
        updatable = false
    )
    @SequenceGenerator(
        name = "primary_sequence",
        sequenceName = "primary_sequence",
        allocationSize = 1,
        initialValue = 10000
    )
    @GeneratedValue(
        strategy = SEQUENCE,
        generator = "primary_sequence"
    )
    var id: Long? = null

    @Column(
        nullable = false,
        length = 75
    )
    var name: String? = null

    @Column
    var monday: Boolean? = null

    @Column
    var tuesday: Boolean? = null

    @Column
    var wednesday: Boolean? = null

    @Column
    var thursday: Boolean? = null

    @Column
    var friday: Boolean? = null

    @Column
    var saturday: Boolean? = null

    @Column
    var sunday: Boolean? = null

    @Column(nullable = false)
    var startTime: LocalTime? = null

    @Column(nullable = false)
    var autoStopMinutes: Int? = null

    @Column
    var active: Boolean? = null

    @ManyToOne(fetch = LAZY)
    @JoinColumn(
        name = "alarm_webradio_id",
        nullable = false
    )
    var alarmWebradio: WebRadio? = null
}

package nl.strmark.piradio.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.SEQUENCE
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator


@Entity
class WebRadio {

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

    @Column(length = 75)
    var name: String? = null

    @Column
    var url: String? = null

    @Column
    var isDefault: Boolean? = null

    @OneToMany(mappedBy = "alarmWebradio")
    var alarmWebradioAlarms: MutableSet<Alarm>? = null
}

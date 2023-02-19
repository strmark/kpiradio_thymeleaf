package nl.strmark.piradio.service

import nl.strmark.piradio.domain.Alarm
import nl.strmark.piradio.model.AlarmDTO
import nl.strmark.piradio.repos.AlarmRepository
import nl.strmark.piradio.repos.WebRadioRepository
import nl.strmark.piradio.util.NotFoundException
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class AlarmService(
    private val alarmRepository: AlarmRepository,
    private val webRadioRepository: WebRadioRepository
) {


    fun findAll(): List<AlarmDTO> {
        val alarms: List<Alarm> = alarmRepository.findAll(Sort.by("id"))
        return alarms.map { alarm -> mapToDTO(alarm, AlarmDTO()) }
    }

    fun findByActive(active: Boolean): List<AlarmDTO> {
        val alarms: List<Alarm> = alarmRepository.findByActive(active)
        return alarms.map { alarm -> mapToDTO(alarm, AlarmDTO()) }
    }

    fun `get`(id: Long): AlarmDTO {
        return alarmRepository.findById(id)
            .map { alarm -> mapToDTO(alarm, AlarmDTO()) }
            .orElseThrow { NotFoundException() }
    }

    fun create(alarmDTO: AlarmDTO): Long {
        val alarm = Alarm()
        mapToEntity(alarmDTO, alarm)
        return alarmRepository.save(alarm).id ?: throw RuntimeException("Save alarms did not return Id")
    }

    fun update(id: Long, alarmDTO: AlarmDTO) {
        val alarm: Alarm = alarmRepository.findById(id)
            .orElseThrow { NotFoundException() }
        mapToEntity(alarmDTO, alarm)
        alarmRepository.save(alarm)
    }

    fun delete(id: Long) {
        alarmRepository.deleteById(id)
    }

    fun mapToDTO(alarm: Alarm, alarmDTO: AlarmDTO): AlarmDTO {
        alarmDTO.id = alarm.id
        alarmDTO.name = alarm.name
        alarmDTO.monday = alarm.monday
        alarmDTO.tuesday = alarm.tuesday
        alarmDTO.wednesday = alarm.wednesday
        alarmDTO.thursday = alarm.thursday
        alarmDTO.friday = alarm.friday
        alarmDTO.saturday = alarm.saturday
        alarmDTO.sunday = alarm.sunday
        alarmDTO.startTime = alarm.startTime
        alarmDTO.autoStopMinutes = alarm.autoStopMinutes
        alarmDTO.active = alarm.active
        alarmDTO.alarmWebradio = alarm.alarmWebradio?.id
        return alarmDTO
    }

    fun mapToEntity(alarmDTO: AlarmDTO, alarm: Alarm): Alarm {
        alarm.name = alarmDTO.name
        alarm.monday = alarmDTO.monday
        alarm.tuesday = alarmDTO.tuesday
        alarm.wednesday = alarmDTO.wednesday
        alarm.thursday = alarmDTO.thursday
        alarm.friday = alarmDTO.friday
        alarm.saturday = alarmDTO.saturday
        alarm.sunday = alarmDTO.sunday
        alarm.startTime = alarmDTO.startTime
        alarm.autoStopMinutes = alarmDTO.autoStopMinutes
        alarm.active = alarmDTO.active
        alarm.alarmWebradio = alarmDTO.alarmWebradio?.let{ webradio ->
            webRadioRepository.findById(webradio)
                .orElseThrow { NotFoundException("alarmWebradio not found") }
        }
        return alarm
    }

}

package no.nav.personbruker.dittnav.eventer.modia.beskjed

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

internal class BeskjedTransformersTest {

    @Test
    fun `skal transformere til DTO`() {
        val beskjed = BeskjedObjectMother.createBeskjed(id = 1, eventId = "123", fodselsnummer = "12345678901", synligFremTil = ZonedDateTime.now(), uid = "abc123", aktiv = true)
        val beskjedDTO = beskjed.toDTO()
        beskjedDTO.uid `should be equal to` beskjed.uid
        beskjedDTO.fodselsnummer `should be equal to` beskjed.fodselsnummer
        beskjedDTO.grupperingsId `should be equal to` beskjed.grupperingsId
        beskjedDTO.eventId `should be equal to` beskjed.eventId
        beskjedDTO.eventTidspunkt `should be equal to` beskjed.eventTidspunkt
        beskjedDTO.produsent `should be equal to` beskjed.produsent
        beskjedDTO.sikkerhetsnivaa `should be equal to` beskjed.sikkerhetsnivaa
        beskjedDTO.sistOppdatert `should be equal to` beskjed.sistOppdatert
        beskjedDTO.synligFremTil `should be equal to` beskjed.synligFremTil
        beskjedDTO.tekst `should be equal to` beskjed.tekst
        beskjedDTO.link `should be equal to` beskjed.link
        beskjedDTO.aktiv `should be equal to` beskjed.aktiv
    }
}

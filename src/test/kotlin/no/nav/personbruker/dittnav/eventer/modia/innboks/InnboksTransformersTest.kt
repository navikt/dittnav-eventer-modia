package no.nav.personbruker.dittnav.eventer.modia.innboks

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class InnboksTransformersTest {

    @Test
    fun `skal transformere til DTO`() {
        val innboks = InnboksObjectMother.createInnboks(id = 1, eventId = "123", fodselsnummer = "12345678901", aktiv = true)
        val innboksDTO = innboks.toDTO()
        innboksDTO.produsent `should be equal to` innboks.produsent
        innboksDTO.eventTidspunkt `should be equal to` innboks.eventTidspunkt
        innboksDTO.fodselsnummer `should be equal to` innboks.fodselsnummer
        innboksDTO.eventId `should be equal to` innboks.eventId
        innboksDTO.grupperingsId `should be equal to` innboks.grupperingsId
        innboksDTO.tekst `should be equal to` innboks.tekst
        innboksDTO.link `should be equal to` innboks.link
        innboksDTO.sikkerhetsnivaa `should be equal to` innboks.sikkerhetsnivaa
        innboksDTO.sistOppdatert `should be equal to` innboks.sistOppdatert
        innboksDTO.aktiv `should be equal to` innboks.aktiv
    }
}

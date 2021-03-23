package no.nav.personbruker.dittnav.eventer.modia.oppgave

import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class OppgaveTransformersTest {

    @Test
    fun `skal transformere til DTO`() {
        val oppgave = OppgaveObjectMother.createOppgave(id = 1, eventId = "123", fodselsnummer = "12345678901", aktiv = true)
        val oppgaveDTO = oppgave.toDTO()
        oppgaveDTO.fodselsnummer `should be equal to` oppgave.fodselsnummer
        oppgaveDTO.grupperingsId `should be equal to` oppgave.grupperingsId
        oppgaveDTO.eventId `should be equal to` oppgave.eventId
        oppgaveDTO.eventTidspunkt `should be equal to` oppgave.eventTidspunkt
        oppgaveDTO.produsent `should be equal to` oppgave.produsent
        oppgaveDTO.sikkerhetsnivaa `should be equal to` oppgave.sikkerhetsnivaa
        oppgaveDTO.sistOppdatert `should be equal to` oppgave.sistOppdatert
        oppgaveDTO.tekst `should be equal to` oppgave.tekst
        oppgaveDTO.link `should be equal to` oppgave.link
        oppgaveDTO.aktiv `should be equal to` oppgave.aktiv
    }
}

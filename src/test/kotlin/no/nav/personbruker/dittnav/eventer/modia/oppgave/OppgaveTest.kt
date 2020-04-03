package no.nav.personbruker.dittnav.eventer.modia.oppgave

import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

class OppgaveTest {

    @Test
    fun `skal returnere maskerte data fra toString-metoden`() {
        val oppgave = OppgaveObjectMother.createOppgave(1, "dummyEventId1", "dummmyFnr1", true)
        val oppgaveAsString = oppgave.toString()
        oppgaveAsString `should contain` "fodselsnummer=***"
        oppgaveAsString `should contain` "tekst=***"
        oppgaveAsString `should contain` "link=***"
    }
}

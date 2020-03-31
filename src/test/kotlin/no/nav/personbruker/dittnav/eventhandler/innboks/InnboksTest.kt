package no.nav.personbruker.dittnav.eventhandler.innboks

import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.Test

class InnboksTest {

    @Test
    fun `skal returnere maskerte data fra toString-metoden`() {
        val innboks = createInnboks(1, "dummyEventId1", "dummmyFnr1", true)
        val innboksAsString = innboks.toString()
        innboksAsString `should contain` "fodselsnummer=***"
        innboksAsString `should contain` "tekst=***"
        innboksAsString `should contain` "link=***"
    }
}

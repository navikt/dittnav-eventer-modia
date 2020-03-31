package no.nav.personbruker.dittnav.eventhandler.done

import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class doneCreatorTest {

    private val fodselsnummer = "123"
    private val grupperingsid = "789"
    private val eventId = "11"
    private val produser = "DittNAV"

    @Test
    fun `should create done-event`() {
        runBlocking {
            val doneEvent = createDoneEvent(fodselsnummer, grupperingsid)
            doneEvent.getFodselsnummer() `should be equal to` fodselsnummer
            doneEvent.getGrupperingsId() `should be equal to` grupperingsid
        }
    }

    @Test
    fun `should create done-key`() {
        runBlocking {
            val doneNnokkel = createKeyForEvent(eventId, produser)
            doneNnokkel.getEventId() `should be equal to` eventId
            doneNnokkel.getSystembruker() `should be equal to` produser
        }
    }
}

package no.nav.personbruker.dittnav.eventhandler.done

import Beskjed
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventhandler.beskjed.createBeskjed
import no.nav.personbruker.dittnav.eventhandler.common.database.H2Database
import no.nav.personbruker.dittnav.eventhandler.common.exceptions.DuplicateEventException
import no.nav.personbruker.dittnav.eventhandler.common.exceptions.NoEventsException
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should throw`
import org.amshove.kluent.invoking
import org.junit.jupiter.api.Test

class DoneEventServiceTest {

    private val database = H2Database()
    private val doneProducer = mockk<DoneProducer>()
    private val doneEventService = DoneEventService(database, doneProducer)
    private val fodselsnummer = "12345"
    private val uid = "1"
    private val eventId = "125"

    @Test
    fun `Kaster exception hvis listen er tom`() {
        val emptyListOfBeskjed = emptyList<Beskjed>()
        invoking {
            runBlocking {
                doneEventService.isEventBeskjedListValid(emptyListOfBeskjed)
            }
        } `should throw` NoEventsException::class
    }

    @Test
    fun `Kaster exception hvis det er duplikat i listen`() {
        val beskjedListDuplicate = listOf<Beskjed>(createBeskjed(1, "dummyEventId1", "dummmyFnr1", null, "dummyUid1", true),
                                                                createBeskjed(1, "dummyEventId1", "dummyFnr1", null, "dummyUid1", true))
        invoking {
            runBlocking {
                doneEventService.isEventBeskjedListValid(beskjedListDuplicate)
            }
        } `should throw` DuplicateEventException::class
    }

    @Test
    fun `should find event that matches input parameter`() {
        runBlocking {
            doneEventService.getBeskjedFromCacheForUser(fodselsnummer, uid, eventId).size `should be equal to` 1
        }
    }

    @Test
    fun `should return empty list if no events exists`() {
        runBlocking {
            doneEventService.getBeskjedFromCacheForUser(fodselsnummer, uid, "dummyEventId").size `should be equal to` 0
        }
    }
}

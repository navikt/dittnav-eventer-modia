package no.nav.personbruker.dittnav.eventer.modia.beskjed

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventer.modia.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BeskjedEventServiceTest {

    private val database = mockk<Database>()
    private val beskjedEventService = BeskjedEventService(database)
    private val bruker = InnloggetBrukerObjectMother.createInnloggetBruker("123")

    private val appender: ListAppender<ILoggingEvent> = ListAppender()
    private val logger: Logger = LoggerFactory.getLogger(BeskjedEventService::class.java) as Logger

    @BeforeAll
    fun `setup`() {
        appender.start()
        logger.addAppender(appender)
    }

    @AfterAll
    fun `teardown`() {
        logger.detachAppender(appender)
    }

    @Test
    fun `Should not filter on expiry date when requesting all Beskjeds`() {
        val beskjedList = getBeskjedList()
        runBlocking {
            coEvery {
                database.queryWithExceptionTranslation<List<Beskjed>>(any())
            }.returns(beskjedList)

            val actualBeskjeds = beskjedEventService.getAllCachedEventsForUser(bruker)
            actualBeskjeds.size `should be equal to` beskjedList.size
        }
    }

    @Test
    fun `Should filter on expiry date when requesting active Beskjeds`() {
        val beskjedList = getBeskjedList()
        runBlocking {
            coEvery {
                database.queryWithExceptionTranslation<List<Beskjed>>(any())
            }.returns(beskjedList)

            val actualBeskjeds = beskjedEventService.getActiveCachedEventsForUser(bruker)
            actualBeskjeds.size `should be equal to` 1
        }
    }

    @Test
    fun `Should return expired as inactive`() {
        val beskjedList = getBeskjedList()
        beskjedList.add(BeskjedObjectMother.createBeskjed(3, "3", bruker.fodselsnummer, null, "123", false))
        beskjedList.add(BeskjedObjectMother.createBeskjed(4, "4", bruker.fodselsnummer, ZonedDateTime.now().minusDays(1), "123", true))
        runBlocking {
            coEvery {
                database.queryWithExceptionTranslation<List<Beskjed>>(any())
            }.returns(beskjedList)

            val actualBeskjeds = beskjedEventService.getInactiveCachedEventsForUser(bruker)
            actualBeskjeds.size `should be equal to` 3
        }
    }

    @Test
    fun `Should log warning if producer is empty`() {
        val beskjedListWithEmptyProducer = listOf(
                BeskjedObjectMother.createBeskjed(1, "1", bruker.fodselsnummer, null, "123", true).copy(produsent = ""))
        runBlocking {
            coEvery {
                database.queryWithExceptionTranslation<List<Beskjed>>(any())
            }.returns(beskjedListWithEmptyProducer)
            beskjedEventService.getActiveCachedEventsForUser(bruker)
        }
        val logevent = appender.list.first()
        logevent.level.levelStr `should be equal to` "WARN"
        logevent.formattedMessage `should contain` "produsent"
    }

    fun getBeskjedList(): MutableList<Beskjed> {
        return mutableListOf(
                BeskjedObjectMother.createBeskjed(1, "1", bruker.fodselsnummer, null, "123", true),
                BeskjedObjectMother.createBeskjed(2, "2", bruker.fodselsnummer, ZonedDateTime.now().minusDays(2), "123", true))
    }
}

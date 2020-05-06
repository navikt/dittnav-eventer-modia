package no.nav.personbruker.dittnav.eventer.modia.innboks

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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InnboksEventServiceTest {

    private val database = mockk<Database>()
    private val innboksEventService = InnboksEventService(database)
    private val bruker = InnloggetBrukerObjectMother.createInnloggetBruker("123")

    private val appender: ListAppender<ILoggingEvent> = ListAppender()
    private val logger: Logger = LoggerFactory.getLogger(InnboksEventService::class.java) as Logger

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
    fun `Should log warning if producer is empty`() {
        val innboksListWithEmptyProducer = listOf(
                InnboksObjectMother.createInnboks(id = 1, eventId = "123", fodselsnummer = "12345", aktiv = true).copy(produsent = ""))
        runBlocking {
            coEvery {
                database.queryWithExceptionTranslation<List<Innboks>>(any())
            }.returns(innboksListWithEmptyProducer)
            innboksEventService.getActiveCachedEventsForUser(bruker)
        }
        val logevent = appender.list.first()
        logevent.level.levelStr `should be equal to` "WARN"
        logevent.formattedMessage `should contain` "produsent"
    }
}

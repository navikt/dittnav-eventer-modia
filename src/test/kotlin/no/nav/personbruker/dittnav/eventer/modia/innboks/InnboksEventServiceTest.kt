package no.nav.personbruker.dittnav.eventer.modia.innboks

import io.mockk.*
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventer.modia.common.AzureToken
import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import no.nav.personbruker.dittnav.eventer.modia.common.InnloggetBrukerObjectMother
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InnboksEventServiceTest {

    private val innboksConsumer: InnboksConsumer = mockk()
    private val tokenFetcher: AzureTokenFetcher = mockk()

    private val innboksEventService = InnboksEventService(innboksConsumer, tokenFetcher)
    private val bruker = InnloggetBrukerObjectMother.createInnloggetBruker("123")

    private val azureToken = AzureToken("tokenValue")

    private val mockedEvents: List<Innboks> = mockk()
    private val transformedEvents: List<InnboksDTO> = mockk()

    @BeforeEach
    fun setupMock() {
        mockkObject(InnboksTransformer)
    }

    @AfterEach
    fun cleanUp() {
        clearMocks(innboksConsumer, tokenFetcher)
    }

    @Test
    fun `should request an azure token and make request on behalf of user for active innboks events`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            innboksConsumer.getActiveEvents(azureToken, bruker.fodselsnummer)
        } returns mockedEvents

        every {
            InnboksTransformer.toInnboksDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            innboksEventService.getActiveCachedEventsForUser(bruker)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { InnboksTransformer.toInnboksDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { innboksConsumer.getActiveEvents(azureToken, bruker.fodselsnummer) }
    }

    @Test
    fun `should request an azure token and make request on behalf of user for inactive innboks events`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            innboksConsumer.getInactiveEvents(azureToken, bruker.fodselsnummer)
        } returns mockedEvents

        every {
            InnboksTransformer.toInnboksDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            innboksEventService.getInactiveCachedEventsForUser(bruker)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { InnboksTransformer.toInnboksDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { innboksConsumer.getInactiveEvents(azureToken, bruker.fodselsnummer) }
    }

    @Test
    fun `should request an azure token and make request on behalf of user for all innboks events`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            innboksConsumer.getAllEvents(azureToken, bruker.fodselsnummer)
        } returns mockedEvents

        every {
            InnboksTransformer.toInnboksDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            innboksEventService.getAllCachedEventsForUser(bruker)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { InnboksTransformer.toInnboksDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { innboksConsumer.getAllEvents(azureToken, bruker.fodselsnummer) }
    }
}

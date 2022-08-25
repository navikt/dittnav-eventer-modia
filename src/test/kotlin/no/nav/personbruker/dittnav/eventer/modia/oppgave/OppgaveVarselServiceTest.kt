package no.nav.personbruker.dittnav.eventer.modia.oppgave

import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventer.modia.common.AzureToken
import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OppgaveVarselServiceTest {

    private val oppgaveConsumer: OppgaveConsumer = mockk()
    private val tokenFetcher: AzureTokenFetcher = mockk()

    private val oppgaveVarselService = OppgaveVarselService(oppgaveConsumer, tokenFetcher)
    private val fnr = "123"

    private val azureToken = AzureToken("tokenValue")

    private val mockedEvents: List<Oppgave> = mockk()
    private val transformedEvents: List<OppgaveDTO> = mockk()

    @BeforeEach
    fun setupMock() {
        mockkObject(OppgaveTransformer)
    }

    @AfterEach
    fun cleanUp() {
        clearMocks(oppgaveConsumer, tokenFetcher)
    }

    @Test
    fun `should request an azure token and make request on behalf of user for active oppgave events`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            oppgaveConsumer.getAktiveVarsler(azureToken, fnr)
        } returns mockedEvents

        every {
            OppgaveTransformer.toOppgaveDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            oppgaveVarselService.aktiveVarsler(fnr)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { OppgaveTransformer.toOppgaveDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { oppgaveConsumer.getAktiveVarsler(azureToken, fnr) }
    }

    @Test
    fun `should request an azure token and make request on behalf of user for inactive oppgave events`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            oppgaveConsumer.getInaktiveVarsler(azureToken, fnr)
        } returns mockedEvents

        every {
            OppgaveTransformer.toOppgaveDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            oppgaveVarselService.inaktiveVarsler(fnr)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { OppgaveTransformer.toOppgaveDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { oppgaveConsumer.getInaktiveVarsler(azureToken, fnr) }
    }

    @Test
    fun `should request an azure token and make request on behalf of user for all oppgave events`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            oppgaveConsumer.getAlleVarsler(azureToken, fnr)
        } returns mockedEvents

        every {
            OppgaveTransformer.toOppgaveDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            oppgaveVarselService.alleVarsler(fnr)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { OppgaveTransformer.toOppgaveDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { oppgaveConsumer.getAlleVarsler(azureToken, fnr) }
    }
}

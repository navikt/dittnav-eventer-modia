package no.nav.personbruker.dittnav.eventer.modia.innboks

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
class InnboksVarselServiceTest {

    private val innboksConsumer: InnboksConsumer = mockk()
    private val tokenFetcher: AzureTokenFetcher = mockk()

    private val innboksVarselService = InnboksVarselService(innboksConsumer, tokenFetcher)
    private val fnr = "123"

    private val azureToken = AzureToken("tokenValue")

    private val mockedVarsler: List<Innboks> = mockk()
    private val transformedVarsler: List<InnboksDTO> = mockk()

    @BeforeEach
    fun setupMock() {
        mockkObject(InnboksTransformer)
    }

    @AfterEach
    fun cleanUp() {
        clearMocks(innboksConsumer, tokenFetcher)
    }

    @Test
    fun `should request an azure token and make request on behalf of user for active innboksvarsler`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            innboksConsumer.getAktiveVarsler(azureToken, fnr)
        } returns mockedVarsler

        every {
            InnboksTransformer.toInnboksDTO(mockedVarsler)
        } returns transformedVarsler

        val result = runBlocking {
            innboksVarselService.aktiveVarsler(fnr)
        }

        result `should be equal to` transformedVarsler

        verify(exactly = 1) { InnboksTransformer.toInnboksDTO(mockedVarsler) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { innboksConsumer.getAktiveVarsler(azureToken, fnr) }
    }

    @Test
    fun `should request an azure token and make request on behalf of user for inactive innboksvarsler`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            innboksConsumer.getInaktiveVarsler(azureToken, fnr)
        } returns mockedVarsler

        every {
            InnboksTransformer.toInnboksDTO(mockedVarsler)
        } returns transformedVarsler

        val result = runBlocking {
            innboksVarselService.inaktiveVarsler(fnr)
        }

        result `should be equal to` transformedVarsler

        verify(exactly = 1) { InnboksTransformer.toInnboksDTO(mockedVarsler) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { innboksConsumer.getInaktiveVarsler(azureToken, fnr) }
    }

    @Test
    fun `should request an azure token and make request on behalf of user for all innboksvarsler`() {
        coEvery {
            tokenFetcher.fetchTokenForEventHandler()
        } returns azureToken

        coEvery {
            innboksConsumer.getAlleVarsler(azureToken, fnr)
        } returns mockedVarsler

        every {
            InnboksTransformer.toInnboksDTO(mockedVarsler)
        } returns transformedVarsler

        val result = runBlocking {
            innboksVarselService.alleVarsler(fnr)
        }

        result `should be equal to` transformedVarsler

        verify(exactly = 1) { InnboksTransformer.toInnboksDTO(mockedVarsler) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForEventHandler() }
        coVerify(exactly = 1) { innboksConsumer.getAlleVarsler(azureToken, fnr) }
    }
}

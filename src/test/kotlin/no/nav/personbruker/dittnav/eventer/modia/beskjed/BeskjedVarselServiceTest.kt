package no.nav.personbruker.dittnav.eventer.modia.beskjed

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
class BeskjedVarselServiceTest {

    private val beskjedConsumer: BeskjedConsumer = mockk()
    private val tokenFetcher: AzureTokenFetcher = mockk()

    private val beskjedVarselService = BeskjedVarselService(beskjedConsumer, tokenFetcher)
    private val fnr = "123"

    private val azureToken = AzureToken("tokenValue")

    private val mockedEvents: List<Beskjed> = mockk()
    private val transformedEvents: List<BeskjedDTO> = mockk()

    @BeforeEach
    fun setupMock() {
        mockkObject(BeskjedTransformer)
    }

    @AfterEach
    fun cleanUp() {
        clearMocks(beskjedConsumer, tokenFetcher)
    }

    @Test
    fun `should request an azure token and make request on behalf of user for active beskjed events`() {
        coEvery {
            tokenFetcher.fetchTokenForVarselHandler()
        } returns azureToken

        coEvery {
            beskjedConsumer.getAktiveVarsler(azureToken, fnr)
        } returns mockedEvents

        every {
            BeskjedTransformer.toBeskjedDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            beskjedVarselService.aktiveVarsler(fnr)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { BeskjedTransformer.toBeskjedDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForVarselHandler() }
        coVerify(exactly = 1) { beskjedConsumer.getAktiveVarsler(azureToken, fnr) }
    }

    @Test
    fun `should request an azure token and make request on behalf of user for inactive beskjed events`() {
        coEvery {
            tokenFetcher.fetchTokenForVarselHandler()
        } returns azureToken

        coEvery {
            beskjedConsumer.getInaktiveVarsler(azureToken, fnr)
        } returns mockedEvents

        every {
            BeskjedTransformer.toBeskjedDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            beskjedVarselService.inaktiveVarsler(fnr)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { BeskjedTransformer.toBeskjedDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForVarselHandler() }
        coVerify(exactly = 1) { beskjedConsumer.getInaktiveVarsler(azureToken, fnr) }
    }

    @Test
    fun `should request an azure token and make request on behalf of user for all beskjed events`() {
        coEvery {
            tokenFetcher.fetchTokenForVarselHandler()
        } returns azureToken

        coEvery {
            beskjedConsumer.getAlleVarsler(azureToken, fnr)
        } returns mockedEvents

        every {
            BeskjedTransformer.toBeskjedDTO(mockedEvents)
        } returns transformedEvents

        val result = runBlocking {
            beskjedVarselService.alleVarsler(fnr)
        }

        result `should be equal to` transformedEvents

        verify(exactly = 1) { BeskjedTransformer.toBeskjedDTO(mockedEvents) }
        coVerify(exactly = 1) { tokenFetcher.fetchTokenForVarselHandler() }
        coVerify(exactly = 1) { beskjedConsumer.getAlleVarsler(azureToken, fnr) }
    }
}

package no.nav.personbruker.dittnav.eventer.modia.innboks

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mockApi
import no.nav.personbruker.dittnav.eventer.modia.common.User
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class InnboksApiTest {

    @Test
    fun `inaktive varlser`() {
        val dummyFnr = "16045571871"
        val innboksEventService = mockk<InnboksEventService>()
        coEvery { innboksEventService.getInactiveCachedEventsForUser(User(dummyFnr)) } returns dummyInnbokser(5)
        testApplication {
            mockApi(innboksEventService = innboksEventService)

            client.get("/dittnav-eventer-modia/fetch/innboks/inaktive") {
                header("fodselsnummer", dummyFnr)
            }.also {
                it.status shouldBeEqualTo OK
                Json.decodeFromString<List<Innboks>>(it.bodyAsText()).size shouldBeEqualTo 5
            }
        }
    }
}

private fun dummyInnbokser(antall: Int = 0): List<Innboks> = Innboks(
    fodselsnummer = "",
    grupperingsId = "",
    eventId = "",
    forstBehandlet = ZonedDateTime.now().minusMinutes(9),
    produsent = "",
    sikkerhetsnivaa = 0,
    sistOppdatert = ZonedDateTime.now(),
    tekst = "",
    link = "",
    aktiv = false,
    eksternVarslingSendt = false,
    eksternVarslingKanaler = emptyList()
).createList(antall = antall)

private fun Innboks.createList(antall: Int): MutableList<Innboks> =
    mutableListOf<Innboks>().also { list ->
        for (i in 1..antall) {
            list.add(this)
        }
    }

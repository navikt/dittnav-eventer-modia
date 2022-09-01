package no.nav.personbruker.dittnav.eventer.modia.beskjed

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.feature
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.mockk.coEvery
import io.mockk.mockk
import mockApi
import no.nav.personbruker.dittnav.eventer.modia.common.User
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class BeskjedApiTest {
    private val objectmapper = ObjectMapper()

    @Test
    fun `setter opp api ruter`() {
        withTestApplication(mockApi()) {
            allRoutes(this.application.feature(Routing)).size shouldBeEqualTo 14
        }
    }

    @Test
    fun `bad request for ugyldig fødselsnummer i header`() {
        withTestApplication(mockApi()) {
            handleRequest {
                handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/beskjed/aktive").also {
                    it.response.status() shouldBeEqualTo BadRequest
                    it.response.content shouldBeEqualTo "Requesten mangler header-en 'fodselsnummer'"
                }
                handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/beskjed/inaktive") {
                    addHeader("fodselsnummer", "1234")
                }.also {
                    it.response.status() shouldBeEqualTo BadRequest
                    it.response.content shouldBeEqualTo "Header-en 'fodselsnummer' inneholder ikke et gyldig fødselsnummer."
                }
            }
        }
    }

    @Test
    fun `inaktive varlser`() {
        val dummyFnr = "16045571871"
        val beskjedEventService = mockk<BeskjedEventService>()
        coEvery { beskjedEventService.getInactiveCachedEventsForUser(User(dummyFnr)) } returns dummyBeskjeder(5)
        withTestApplication(mockApi(beskjedEventService = beskjedEventService)) {
            handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/beskjed/inaktive") {
                addHeader("fodselsnummer", dummyFnr)
            }.also {
                it.response.status() shouldBeEqualTo OK
                objectmapper.readTree(it.response.content).size() shouldBeEqualTo 5
            }
        }
    }
}

private fun dummyBeskjeder(antall: Int = 0): List<Beskjed> = Beskjed(
    fodselsnummer = "",
    grupperingsId = "",
    eventId = "",
    forstBehandlet = ZonedDateTime.now().minusMinutes(9),
    produsent = "",
    sikkerhetsnivaa = 0,
    sistOppdatert = ZonedDateTime.now(),
    synligFremTil = ZonedDateTime.now().plusDays(1),
    tekst = "",
    link = "",
    aktiv = false,
    eksternVarslingSendt = false,
    eksternVarslingKanaler = emptyList()
).createList(antall = antall)

private fun Beskjed.createList(antall: Int): MutableList<Beskjed> =
    mutableListOf<Beskjed>().also { list ->
        for (i in 1..antall) {
            list.add(this)
        }
    }

fun allRoutes(root: Route): List<Route> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
        .filter { it.toString().contains("method") && it.toString() != "/" }
}

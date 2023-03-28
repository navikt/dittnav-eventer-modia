package no.nav.personbruker.dittnav.eventer.modia.oppgave

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

class OppgaveApiTest {
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
                handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/oppgave/aktive").also {
                    it.response.status() shouldBeEqualTo BadRequest
                    it.response.content shouldBeEqualTo "Requesten mangler header-en 'fodselsnummer'"
                }
                handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/oppgave/inaktive") {
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
        val oppgaveEventService = mockk<OppgaveEventService>()
        coEvery { oppgaveEventService.getInactiveCachedEventsForUser(User(dummyFnr)) } returns dummyOppgaveer(5)
        withTestApplication(mockApi(oppgaveEventService = oppgaveEventService)) {
            handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/oppgave/inaktive") {
                addHeader("fodselsnummer", dummyFnr)
            }.also {
                it.response.status() shouldBeEqualTo OK
                objectmapper.readTree(it.response.content).size() shouldBeEqualTo 5
            }
        }
    }
}

private fun dummyOppgaveer(antall: Int = 0): List<Oppgave> = Oppgave(
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
    eksternVarsling = null
).createList(antall = antall)

private fun Oppgave.createList(antall: Int): MutableList<Oppgave> =
    mutableListOf<Oppgave>().also { list ->
        for (i in 1..antall) {
            list.add(this)
        }
    }

fun allRoutes(root: Route): List<Route> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
        .filter { it.toString().contains("method") && it.toString() != "/" }
}

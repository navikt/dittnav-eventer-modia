package no.nav.personbruker.dittnav.eventer.modia.oppgave

import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.Application
import io.ktor.server.routing.Route
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

class OppgaveApiTest {
    @Test
    fun `inaktive varlser`() {
        val dummyFnr = "16045571871"
        val oppgaveEventService = mockk<OppgaveEventService>()
        coEvery { oppgaveEventService.getInactiveCachedEventsForUser(User(dummyFnr)) } returns dummyOppgaveer(5)
        testApplication {
            mockApi(oppgaveEventService = oppgaveEventService)
            client.get("/dittnav-eventer-modia/fetch/oppgave/inaktive") {
                headers {
                    accept(ContentType.Application.Json)
                    header("fodselsnummer", dummyFnr)
                }
            }.also {
                it.status shouldBeEqualTo OK
                Json.decodeFromString<List<Oppgave>>(it.bodyAsText()).size shouldBeEqualTo 5
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
    eksternVarslingSendt = false,
    eksternVarslingKanaler = emptyList()
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

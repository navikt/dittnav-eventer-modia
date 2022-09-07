package no.nav.personbruker.dittnav.eventer.modia.beskjed

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode.Companion.OK
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

class BeskjedApiTest {

    @Test
    fun `inaktive varlser`() {
        val dummyFnr = "16045571871"
        val beskjedEventService = mockk<BeskjedEventService>()
        coEvery { beskjedEventService.getInactiveCachedEventsForUser(User(dummyFnr)) } returns dummyBeskjeder(5)
        testApplication {
            (mockApi(beskjedEventService = beskjedEventService))
            client.get("/dittnav-eventer-modia/fetch/beskjed/inaktive") {
                header("fodselsnummer", dummyFnr)
            }.also {
                it.status shouldBeEqualTo OK
                (Json.decodeFromString(it.bodyAsText()) as List<Beskjed>).size shouldBeEqualTo 5
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

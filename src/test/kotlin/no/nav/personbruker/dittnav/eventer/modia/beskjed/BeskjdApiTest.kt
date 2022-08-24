package no.nav.personbruker.dittnav.eventer.modia.beskjed

import io.ktor.application.feature
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.routing.Route
import io.ktor.routing.Routing
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import mockApi
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class BeskjdApiTest {

    @Test
    fun `setter opp api ruter`() {
        withTestApplication(mockApi()) {
            println(allRoutes(this.application.feature(Routing)))
            allRoutes(this.application.feature(Routing)).size shouldBeEqualTo 14
        }
    }

    @Test
    fun `bad request for ugyldig fødselsnummer i header`() {
        withTestApplication(mockApi()) {
            handleRequest {
                handleRequest(HttpMethod.Get, "/fetch/beskjed/aktive").also {
                    it.response.status() shouldBeEqualTo BadRequest
                    it.response.content shouldBeEqualTo "Requesten mangler header-en 'fodselsnummer'"
                }
                handleRequest(HttpMethod.Get, "/fetch/beskjed/inaktive") {
                    addHeader("fodselsnummer", "1234")
                }.also {
                    it.response.status() shouldBeEqualTo BadRequest
                    it.response.content shouldBeEqualTo "Header-en 'fodselsnummer' inneholder ikke et gyldig fødselsnummer."
                }
            }
        }
    }
}

fun allRoutes(root: Route): List<Route> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
        .filter { it.toString().contains("method") && it.toString() != "/" }
}

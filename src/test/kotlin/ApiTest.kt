import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.plugin
import io.ktor.server.routing.Routing
import io.ktor.server.testing.testApplication
import no.nav.personbruker.dittnav.eventer.modia.oppgave.allRoutes
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ApiTest {
    @Test
    fun `setter opp api ruter`() {
        testApplication() {
            mockApi()
            application {
                allRoutes(plugin(Routing)).size shouldBeEqualTo 14
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["beskjed", "oppgave", "innboks"])
    fun `bad request for ugyldig fødselsnummer i header`(type: String) {
        // /(method:GET)
        testApplication {
            mockApi()
            client.get("/dittnav-eventer-modia/fetch/$type/aktive").also {
                it.status shouldBeEqualTo HttpStatusCode.BadRequest
                it.bodyAsText() shouldBeEqualTo "Requesten mangler header-en 'fodselsnummer'"
            }
            client.get("/dittnav-eventer-modia/fetch/$type/inaktive") {
                header("fodselsnummer", "1234")
            }.also {
                it.status shouldBeEqualTo HttpStatusCode.BadRequest
                it.bodyAsText() shouldBeEqualTo "Header-en 'fodselsnummer' inneholder ikke et gyldig fødselsnummer."
            }
        }
    }
}

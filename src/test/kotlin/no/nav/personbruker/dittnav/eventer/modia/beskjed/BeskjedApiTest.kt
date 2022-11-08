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
import jsonArray
import mockApi
import mockEngine
import no.nav.personbruker.dittnav.eventer.modia.common.AzureToken
import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import testHttpClient
import times
import java.net.URL
import java.time.ZonedDateTime

class BeskjedApiTest {
    private val objectmapper = ObjectMapper()
    private val eventhandlertestURL = "http://eventhandler.test"
    private val activeEventsEndpoint = "$eventhandlertestURL/fetch/modia/beskjed/aktive"
    private val inactiveEventsEndpoint = "$eventhandlertestURL/fetch/modia/beskjed/inaktive"
    private val allEventsEndpoint = "$eventhandlertestURL/fetch/modia/beskjed/all"
    private val mockAzureService = mockk<AzureTokenFetcher>().also {
        coEvery { it.fetchTokenForEventHandler() } returns AzureToken("dummytoken")
    }

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
        val (eventhandlerRespons, forventedebeskjeder) = mockContent(size = 5)
        val dummyFnr = "16045571871"
        val beskjedEventService = BeskjedEventService(
            beskjedConsumer = BeskjedConsumer(
                client = testHttpClient(mockEngine(inactiveEventsEndpoint, eventhandlerRespons)),
                eventHandlerBaseURL = URL(eventhandlertestURL)
            ),
            azureTokenFetcher = mockAzureService
        )

        withTestApplication(mockApi(beskjedEventService = beskjedEventService)) {
            handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/beskjed/inaktive") {
                addHeader("fodselsnummer", dummyFnr)
            }.apply {
                response.status() shouldBeEqualTo OK
                objectmapper.readTree(response.content).apply {
                    size() shouldBe 5
                    map { it["eventId"].asText() } shouldContainAll forventedebeskjeder.map { it.eventId }
                }
            }
        }
    }

    @Test
    fun `aktive beskjeder`() {
        val (eventhandlerRespons, forventedebeskjeder) = mockContent(size = 1)
        val dummyFnr = "16045571871"
        val beskjedEventService = BeskjedEventService(
            beskjedConsumer = BeskjedConsumer(
                client = testHttpClient(mockEngine(activeEventsEndpoint, eventhandlerRespons)),
                eventHandlerBaseURL = URL(eventhandlertestURL)
            ),
            azureTokenFetcher = mockAzureService
        )

        withTestApplication(mockApi(beskjedEventService = beskjedEventService)) {
            handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/beskjed/aktive") {
                addHeader("fodselsnummer", dummyFnr)
            }.apply {
                response.status() shouldBeEqualTo OK
                objectmapper.readTree(response.content).apply {
                    size() shouldBe 1
                    map { it["eventId"].asText() } shouldContainAll forventedebeskjeder.map { it.eventId }
                }
            }
        }
    }

    @Test
    fun `alle beskjeder`() {
        val (eventhandlerRespons, forventedebeskjeder) = mockContent(size = 12)
        val dummyFnr = "16045571871"
        val beskjedEventService = BeskjedEventService(
            beskjedConsumer = BeskjedConsumer(
                client = testHttpClient(mockEngine(allEventsEndpoint, eventhandlerRespons)),
                eventHandlerBaseURL = URL(eventhandlertestURL)
            ),
            azureTokenFetcher = mockAzureService
        )

        withTestApplication(mockApi(beskjedEventService = beskjedEventService)) {
            handleRequest(HttpMethod.Get, "/dittnav-eventer-modia/fetch/beskjed/all") {
                addHeader("fodselsnummer", dummyFnr)
            }.apply {
                response.status() shouldBeEqualTo OK
                objectmapper.readTree(response.content).apply {
                    size() shouldBe 12
                    map { it["eventId"].asText() } shouldContainAll forventedebeskjeder.map { it.eventId }
                }
            }
        }
    }
}

fun allRoutes(root: Route): List<Route> {
    return listOf(root) + root.children.flatMap { allRoutes(it) }
        .filter { it.toString().contains("method") && it.toString() != "/" }
}

private fun mockContent(
    førstBehandlet: ZonedDateTime = ZonedDateTime.now().minusDays(1),
    sistOppdatert: ZonedDateTime = ZonedDateTime.now(),
    synligFremTil: ZonedDateTime? = null,
    size: Int
): Pair<String, List<Beskjed>> {
    val synligFremTilString = synligFremTil?.let {
        """"${synligFremTil.withFixedOffsetZone()}""""
    } ?: "null"
    return Pair(
        """  {
        "fodselsnummer": "123",
        "grupperingsId": "",
        "eventId": "1988",
        "forstBehandlet": "${førstBehandlet.withFixedOffsetZone()}",
        "produsent": "",
        "sikkerhetsnivaa": 0,
        "sistOppdatert": "${sistOppdatert.withFixedOffsetZone()}",
        "synligFremTil": $synligFremTilString
        "tekst": "Tadda vi tester",
        "link": "",
        "appnavn": "appappapp",
        "aktiv": false,
        "eksternVarslingSendt": true,
        "ukjentFelt": "tulleverdi",
        "eksternVarslingKanaler":["SMS", "EPOST"]
      }""".jsonArray(size),
        Beskjed(
            fodselsnummer = "123",
            grupperingsId = "",
            eventId = "1988",
            forstBehandlet = førstBehandlet.withFixedOffsetZone(),
            produsent = "",
            sikkerhetsnivaa = 0,
            sistOppdatert = sistOppdatert.withFixedOffsetZone(),
            tekst = "Tadda vi tester",
            link = "",
            aktiv = false,
            synligFremTil = synligFremTil?.withFixedOffsetZone(),
            eksternVarslingSendt = true,
            eksternVarslingKanaler = listOf("SMS", "EPOST")
        ) * size
    )
}

import io.ktor.application.Application
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.mockk
import no.nav.personbruker.dittnav.eventer.modia.beskjed.Beskjed
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedEventService
import no.nav.personbruker.dittnav.eventer.modia.config.api
import no.nav.personbruker.dittnav.eventer.modia.config.jsonConfig
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksEventService
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveEventService
import no.nav.tms.token.support.authentication.installer.mock.installMockedAuthenticators
import no.nav.tms.token.support.tokenx.validation.mock.SecurityLevel

fun mockApi(
    authConfig: Application.() -> Unit = mockAuthBuilder(),
    oppgaveEventService: OppgaveEventService = mockk(relaxed = true),
    innboksEventService: InnboksEventService = mockk(relaxed = true),
    beskjedEventService: BeskjedEventService = mockk(relaxed = true)

): Application.() -> Unit {
    return fun Application.() {
        authConfig()
        api(
            authConfig = {},
            oppgaveEventService = oppgaveEventService,
            innboksEventService = innboksEventService,
            beskjedEventService = beskjedEventService
        )
    }
}

fun mockAuthBuilder(): Application.() -> Unit = {
    installMockedAuthenticators {
        installTokenXAuthMock {
            setAsDefault = true

            alwaysAuthenticated = true
            staticUserPid = "123"
            staticSecurityLevel = SecurityLevel.LEVEL_4
        }
        installAzureAuthMock { }
    }
}

fun testHttpClient(mockEngine: MockEngine, jsonSerializer: KotlinxSerializer = KotlinxSerializer(jsonConfig())): HttpClient {
    return HttpClient(mockEngine) {
        install(JsonFeature) {
            serializer = jsonSerializer
        }
        install(HttpTimeout)
    }
}

fun mockEngine(expectedUrl: String, content: String) = MockEngine { request ->
    if (request.url.sameAs(expectedUrl)) {
        respond(
            content = ByteReadChannel(content),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    } else {
        respond(
            content = ByteReadChannel("endepunktet finnes ikke"),
            status = HttpStatusCode.NotFound
        )
    }
}

private fun Url.sameAs(other: String): Boolean {
    val otherUrl = Url(other)
    return host == otherUrl.host && this.fullPath == otherUrl.fullPath
}
internal fun String.jsonArray(size: Int): String =
    (1..size).joinToString(separator = ",", prefix = "[", postfix = "]") { this }

internal operator fun Beskjed.times(size: Int): List<Beskjed> = mutableListOf<Beskjed>().also { list ->
    for (i in 1..size) {
        list.add(this)
    }
}

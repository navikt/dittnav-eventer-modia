package no.nav.personbruker.dittnav.eventer.modia

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.engine.embeddedServer
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedConsumer
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedEventService
import no.nav.personbruker.dittnav.eventer.modia.beskjed.beskjedApi
import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import no.nav.personbruker.dittnav.eventer.modia.common.healthApi
import no.nav.personbruker.dittnav.eventer.modia.config.Environment
import no.nav.personbruker.dittnav.eventer.modia.config.HttpClientBuilder
import no.nav.personbruker.dittnav.eventer.modia.config.issoAuthenticationBuilder
import no.nav.personbruker.dittnav.eventer.modia.config.jsonConfig
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksEventService
import no.nav.personbruker.dittnav.eventer.modia.innboks.innboksApi
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveEventService
import no.nav.personbruker.dittnav.eventer.modia.oppgave.oppgaveApi
import no.nav.tms.token.support.azure.exchange.AzureServiceBuilder
import java.net.URL

fun main() {

    val environment = Environment()

    val azureService = AzureServiceBuilder.buildAzureService(enableDefaultProxy = true)
    val azureTokenFetcher = AzureTokenFetcher(azureService, environment.eventHandlerClientId)

    val httpClient = HttpClientBuilder.build()

    val beskjedConsumer = BeskjedConsumer(httpClient, URL(environment.eventHandlerUrl))
    val beskjedEventService = BeskjedEventService(beskjedConsumer, azureTokenFetcher)

    val oppgaveConsumer = OppgaveConsumer(httpClient, URL(environment.eventHandlerUrl))
    val oppgaveEventService = OppgaveEventService(oppgaveConsumer, azureTokenFetcher)

    val innboksConsumer = InnboksConsumer(httpClient, URL(environment.eventHandlerUrl))
    val innboksEventService = InnboksEventService(innboksConsumer, azureTokenFetcher)

    embeddedServer(Netty, port = 8080) {
        api(
            authConfig = issoAuthenticationBuilder(environment),
            oppgaveEventService = oppgaveEventService,
            beskjedEventService = beskjedEventService,
            innboksEventService = innboksEventService
        )
    }.start(wait = true)
}

fun Application.api(
    authConfig: Application.() -> Unit,
    oppgaveEventService: OppgaveEventService,
    innboksEventService: InnboksEventService,
    beskjedEventService: BeskjedEventService
) {
    val prometheusMeterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(DefaultHeaders)
    install(ContentNegotiation) {
        json(jsonConfig())
    }
    install(MicrometerMetrics) {
        registry = prometheusMeterRegistry
    }
    authConfig()

    routing {
        route("/dittnav-eventer-modia") {
            route("internal") {
                healthApi(prometheusMeterRegistry)
            }

            authenticate {
                oppgaveApi(oppgaveEventService)
                innboksApi(innboksEventService)
                beskjedApi(beskjedEventService)
            }
        }
    }
}

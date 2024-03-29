package no.nav.personbruker.dittnav.eventer.modia.config

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedConsumer
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedEventService
import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksConsumer
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksEventService
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveConsumer
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveEventService
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

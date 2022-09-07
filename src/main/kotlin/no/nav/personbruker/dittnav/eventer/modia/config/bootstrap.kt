package no.nav.personbruker.dittnav.eventer.modia.config

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authenticate
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedEventService
import no.nav.personbruker.dittnav.eventer.modia.beskjed.beskjedApi
import no.nav.personbruker.dittnav.eventer.modia.common.healthApi
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksEventService
import no.nav.personbruker.dittnav.eventer.modia.innboks.innboksApi
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveEventService
import no.nav.personbruker.dittnav.eventer.modia.oppgave.oppgaveApi

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

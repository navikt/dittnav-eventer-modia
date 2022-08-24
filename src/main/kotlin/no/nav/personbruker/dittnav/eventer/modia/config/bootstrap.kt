package no.nav.personbruker.dittnav.eventer.modia.config

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.authenticate
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.prometheus.client.hotspot.DefaultExports
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
    DefaultExports.initialize()
    install(DefaultHeaders)
    install(ContentNegotiation) {
        json()
    }
    authConfig()

    routing {
        route("internal") {
            healthApi()
        }
        authenticate {
            oppgaveApi(oppgaveEventService)
            innboksApi(innboksEventService)
            beskjedApi(beskjedEventService)
        }
    }
}

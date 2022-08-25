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
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedVarselService
import no.nav.personbruker.dittnav.eventer.modia.beskjed.beskjedApi
import no.nav.personbruker.dittnav.eventer.modia.common.healthApi
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksVarselService
import no.nav.personbruker.dittnav.eventer.modia.innboks.innboksApi
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveVarselService
import no.nav.personbruker.dittnav.eventer.modia.oppgave.oppgaveApi

fun Application.api(
    authConfig: Application.() -> Unit,
    oppgaveVarselService: OppgaveVarselService,
    innboksVarselService: InnboksVarselService,
    beskjedVarselService: BeskjedVarselService
) {
    DefaultExports.initialize()
    install(DefaultHeaders)
    install(ContentNegotiation) {
        json()
    }
    authConfig()

    routing {
        route("/dittnav-eventer-modia") {
            route("internal") {
                healthApi()
            }

            authenticate {
                oppgaveApi(oppgaveVarselService)
                innboksApi(innboksVarselService)
                beskjedApi(beskjedVarselService)
            }
        }
    }
}

package no.nav.personbruker.dittnav.eventer.modia.config

import io.ktor.application.Application
import io.ktor.application.ApplicationStopPreparing
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.prometheus.client.hotspot.DefaultExports
import no.nav.personbruker.dittnav.eventer.modia.beskjed.beskjedApi
import no.nav.personbruker.dittnav.eventer.modia.common.healthApi
import no.nav.personbruker.dittnav.eventer.modia.innboks.innboksApi
import no.nav.personbruker.dittnav.eventer.modia.oppgave.oppgaveApi

fun Application.mainModule(appContext: ApplicationContext = ApplicationContext()) {
    DefaultExports.initialize()
    install(DefaultHeaders)

    install(ContentNegotiation) {
        json()
    }

    install(Authentication) {
        jwt {
            setupIssoAuthentication(appContext.environment)
        }
    }

    routing {
        route("internal") {
            healthApi(appContext.database)
        }
        authenticate {
            oppgaveApi(appContext.oppgaveEventService)
            beskjedApi(appContext.beskjedEventService)
            innboksApi(appContext.innboksEventService)
        }
    }

    configureShutdownHook(appContext)
}

private fun Application.configureShutdownHook(appContext: ApplicationContext) {
    environment.monitor.subscribe(ApplicationStopPreparing) {
        closeTheDatabaseConectionPool(appContext)
    }
}

private fun closeTheDatabaseConectionPool(appContext: ApplicationContext) {
    appContext.database.dataSource.close()
}

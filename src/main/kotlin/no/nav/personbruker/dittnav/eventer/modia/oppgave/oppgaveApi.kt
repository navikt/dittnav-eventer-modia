package no.nav.personbruker.dittnav.eventer.modia.oppgave

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.respondWithError
import no.nav.personbruker.dittnav.eventer.modia.config.doIfValidRequest
import org.slf4j.LoggerFactory

fun Route.oppgaveApi(oppgaveVarselService: OppgaveVarselService) {

    val log = LoggerFactory.getLogger(OppgaveVarselService::class.java)

    get("/fetch/oppgave/aktive") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, oppgaveVarselService.aktiveVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/oppgave/inaktive") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, oppgaveVarselService.inaktiveVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/oppgave/all") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, oppgaveVarselService.alleVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }
}

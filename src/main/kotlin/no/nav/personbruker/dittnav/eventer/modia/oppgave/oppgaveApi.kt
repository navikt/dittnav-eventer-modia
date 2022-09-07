package no.nav.personbruker.dittnav.eventer.modia.oppgave

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.respondWithError
import no.nav.personbruker.dittnav.eventer.modia.config.doIfValidRequest
import org.slf4j.LoggerFactory

fun Route.oppgaveApi(oppgaveEventService: OppgaveEventService) {

    val log = LoggerFactory.getLogger(OppgaveEventService::class.java)

    get("/fetch/oppgave/aktive") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val aktiveOppgaveEvents = oppgaveEventService.getActiveCachedEventsForUser(userToFetchEventsFor)
                call.respond(aktiveOppgaveEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/oppgave/inaktive") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val inaktiveOppgaveEvents = oppgaveEventService.getInactiveCachedEventsForUser(userToFetchEventsFor)
                call.respond(inaktiveOppgaveEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/oppgave/all") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val oppgaveEvents = oppgaveEventService.getAllCachedEventsForUser(userToFetchEventsFor)
                call.respond(oppgaveEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }
}

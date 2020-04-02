package no.nav.personbruker.dittnav.eventer.modia.oppgave

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.respondWithError
import org.slf4j.LoggerFactory
import java.lang.Exception

fun Route.oppgaveApi(oppgaveEventService: OppgaveEventService) {

    val log = LoggerFactory.getLogger(OppgaveEventService::class.java)

    get("/fetch/oppgave/aktive") {
        try {
            val userToFetchEventsFor = User("000")
            val aktiveOppgaveEvents = oppgaveEventService.getActiveCachedEventsForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, aktiveOppgaveEvents)
        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/fetch/oppgave/inaktive") {
        try {
            val userToFetchEventsFor = User("000")
            val inaktiveOppgaveEvents = oppgaveEventService.getInactiveCachedEventsForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, inaktiveOppgaveEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/fetch/oppgave/all") {
        try {
            val userToFetchEventsFor = User("000")
            val oppgaveEvents = oppgaveEventService.getAllCachedEventsForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, oppgaveEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}

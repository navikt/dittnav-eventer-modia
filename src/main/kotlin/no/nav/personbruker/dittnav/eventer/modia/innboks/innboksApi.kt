package no.nav.personbruker.dittnav.eventer.modia.innboks

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.respondWithError
import org.slf4j.LoggerFactory
import java.lang.Exception

fun Route.innboksApi(innboksEventService: InnboksEventService) {

    val log = LoggerFactory.getLogger(InnboksEventService::class.java)

    get("/fetch/innboks/aktive") {
        try {
            val userToFetchEventsFor = call.receive<User>()
            val aktiveInnboksEvents = innboksEventService.getActiveCachedEventsForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, aktiveInnboksEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/fetch/innboks/inaktive") {
        try {
            val userToFetchEventsFor = call.receive<User>()
            val inaktiveInnboksEvents = innboksEventService.getInctiveCachedEventsForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, inaktiveInnboksEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/fetch/innboks/all") {
        try {
            val userToFetchEventsFor = call.receive<User>()
            val innboksEvents = innboksEventService.getAllCachedEventsForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, innboksEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}

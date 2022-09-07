package no.nav.personbruker.dittnav.eventer.modia.innboks

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.respondWithError
import no.nav.personbruker.dittnav.eventer.modia.config.doIfValidRequest
import org.slf4j.LoggerFactory

fun Route.innboksApi(innboksEventService: InnboksEventService) {

    val log = LoggerFactory.getLogger(InnboksEventService::class.java)

    get("/fetch/innboks/aktive") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val aktiveInnboksEvents = innboksEventService.getActiveCachedEventsForUser(userToFetchEventsFor)
                call.respond(HttpStatusCode.OK, aktiveInnboksEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/innboks/inaktive") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val inaktiveInnboksEvents = innboksEventService.getInactiveCachedEventsForUser(userToFetchEventsFor)
                call.respond(HttpStatusCode.OK, inaktiveInnboksEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/innboks/all") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val innboksEvents = innboksEventService.getAllCachedEventsForUser(userToFetchEventsFor)
                call.respond(HttpStatusCode.OK, innboksEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }
}

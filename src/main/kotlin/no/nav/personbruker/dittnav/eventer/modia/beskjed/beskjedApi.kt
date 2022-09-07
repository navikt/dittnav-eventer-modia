package no.nav.personbruker.dittnav.eventer.modia.beskjed

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.respondWithError
import no.nav.personbruker.dittnav.eventer.modia.config.doIfValidRequest
import org.slf4j.LoggerFactory

fun Route.beskjedApi(beskjedEventService: BeskjedEventService) {

    val log = LoggerFactory.getLogger(BeskjedEventService::class.java)

    get("/fetch/beskjed/aktive") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val aktiveBeskjedEvents = beskjedEventService.getActiveCachedEventsForUser(userToFetchEventsFor)
                call.respond(aktiveBeskjedEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/beskjed/inaktive") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val inaktiveBeskjedEvents = beskjedEventService.getInactiveCachedEventsForUser(userToFetchEventsFor)
                call.respond(inaktiveBeskjedEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/beskjed/all") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val beskjedEvents = beskjedEventService.getAllCachedEventsForUser(userToFetchEventsFor)
                call.respond(beskjedEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }
}

package no.nav.personbruker.dittnav.eventer.modia.beskjed

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.respondWithError
import org.slf4j.LoggerFactory
import java.lang.Exception

fun Route.beskjedApi(beskjedEventService: BeskjedEventService) {

    val log = LoggerFactory.getLogger(BeskjedEventService::class.java)

    get("/fetch/beskjed/aktive") {
        try {
            val userToFetchEventsFor = User("000")
            val aktiveBeskjedEvents = beskjedEventService.getActiveCachedEventsForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, aktiveBeskjedEvents)
        } catch (exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/fetch/beskjed/inaktive") {
        try {
            val userToFetchEventsFor = User("000")
            val inaktiveBeskjedEvents = beskjedEventService.getInactiveCachedEventsForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, inaktiveBeskjedEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }

    get("/fetch/beskjed/all") {
        try {
            val userToFetchEventsFor = User("000")
            val beskjedEvents = beskjedEventService.getAllEventsFromCacheForUser(userToFetchEventsFor)
            call.respond(HttpStatusCode.OK, beskjedEvents)
        } catch(exception: Exception) {
            respondWithError(call, log, exception)
        }
    }
}

package no.nav.personbruker.dittnav.eventer.modia.beskjed

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.respondWithError
import no.nav.personbruker.dittnav.eventer.modia.config.doIfValidRequest
import org.slf4j.LoggerFactory

fun Route.beskjedApi(beskjedFetcher: BeskjedFetcher) {

    val log = LoggerFactory.getLogger(BeskjedFetcher::class.java)

    get("/fetch/beskjed/aktive") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val aktiveBeskjedEvents = beskjedFetcher.getActiveCachedEventsForUser(userToFetchEventsFor)
                call.respond(HttpStatusCode.OK, aktiveBeskjedEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/beskjed/inaktive") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val inaktiveBeskjedEvents = beskjedFetcher.getInactiveCachedEventsForUser(userToFetchEventsFor)
                call.respond(HttpStatusCode.OK, inaktiveBeskjedEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/beskjed/all") {
        doIfValidRequest { userToFetchEventsFor ->
            try {
                val beskjedEvents = beskjedFetcher.getAllCachedEventsForUser(userToFetchEventsFor)
                call.respond(HttpStatusCode.OK, beskjedEvents)
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }
}

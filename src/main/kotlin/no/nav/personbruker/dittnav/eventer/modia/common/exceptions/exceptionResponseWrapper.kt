package no.nav.personbruker.dittnav.eventer.modia.common.exceptions

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.database.RetriableDatabaseException
import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.database.UnretriableDatabaseException
import org.slf4j.Logger

suspend fun respondWithError(call: ApplicationCall, log: Logger, exception: Exception) {
    when(exception) {
        is EventCacheException -> {
            call.respond(HttpStatusCode.ServiceUnavailable)
            log.warn("Klarte ikke hente eventer fra cache. Returnerer feilkode til frontend. {}", exception.toString(), exception)
        }
        is RetriableDatabaseException -> {
            call.respond(HttpStatusCode.ServiceUnavailable)
            val msg = "Periodisk feil mot databasen, klarte ikke hente eventer fra cache. Returnerer feilkode til frontend. {}"
            log.error(msg, exception.toString(), exception)
        }
        is UnretriableDatabaseException -> {
            call.respond(HttpStatusCode.ServiceUnavailable)
            val msg = "Fikk ikke kontakt med databasen, klarte ikke hente eventer fra cache. Returnerer feilkode til frontend. {}"
            log.error(msg, exception.toString(), exception)
        }
        else -> {
            call.respond(HttpStatusCode.InternalServerError)
            log.error("Ukjent feil oppstod ved henting av eventer fra cache. Returnerer feilkode til frontend", exception)
        }
    }
}

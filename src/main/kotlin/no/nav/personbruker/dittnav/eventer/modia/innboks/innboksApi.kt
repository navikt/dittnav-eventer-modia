package no.nav.personbruker.dittnav.eventer.modia.innboks

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.respondWithError
import no.nav.personbruker.dittnav.eventer.modia.config.doIfValidRequest
import org.slf4j.LoggerFactory

fun Route.innboksApi(innboksVarselService: InnboksVarselService) {

    val log = LoggerFactory.getLogger(InnboksVarselService::class.java)

    get("/fetch/innboks/aktive") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, innboksVarselService.aktiveVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/innboks/inaktive") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, innboksVarselService.inaktiveVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/innboks/all") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, innboksVarselService.alleVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }
}

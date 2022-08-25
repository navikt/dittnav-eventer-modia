package no.nav.personbruker.dittnav.eventer.modia.beskjed

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import no.nav.personbruker.dittnav.eventer.modia.common.respondWithError
import no.nav.personbruker.dittnav.eventer.modia.config.doIfValidRequest
import org.slf4j.LoggerFactory

fun Route.beskjedApi(beskjedVarselService: BeskjedVarselService) {

    val log = LoggerFactory.getLogger(BeskjedVarselService::class.java)

    get("/fetch/beskjed/aktive") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, beskjedVarselService.aktiveVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/beskjed/inaktive") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, beskjedVarselService.inaktiveVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }

    get("/fetch/beskjed/all") {
        doIfValidRequest { fnr ->
            try {
                call.respond(HttpStatusCode.OK, beskjedVarselService.alleVarsler(fnr))
            } catch (exception: Exception) {
                respondWithError(call, log, exception)
            }
        }
    }
}

package no.nav.personbruker.dittnav.eventer.modia.config

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import org.slf4j.LoggerFactory

val log = LoggerFactory.getLogger("requestValidation.tk")

suspend inline fun PipelineContext<Unit, ApplicationCall>.doIfValidRequest(handler: (fnr: String) -> Unit) {
    val headerName = "fodselsnummer"
    val fnrHeader = call.request.headers[headerName]

    when {
        fnrHeader == null -> this.respondWithBadRequest(message = "Requesten mangler header-en '$headerName'")
        !isFodselsnummerOfValidLength(fnrHeader) -> respondWithBadRequest(message = "Header-en '$headerName' inneholder ikke et gyldig fødselsnummer.")
        else -> handler.invoke(fnrHeader)
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.respondWithBadRequest(message: String) {
    log.warn(message)
    call.respond(HttpStatusCode.BadRequest, message)
}

fun isFodselsnummerOfValidLength(fnrHeader: String) = fnrHeader.isNotEmpty() && fnrHeader.length == 11

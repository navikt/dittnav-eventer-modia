package no.nav.personbruker.dittnav.eventhandler.done

import io.ktor.http.HttpStatusCode

data class DoneResponse(
        val message: String,
        val httpStatus: HttpStatusCode
)

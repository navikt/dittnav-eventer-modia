package no.nav.personbruker.dittnav.eventhandler.done

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.util.pipeline.PipelineContext
import no.nav.personbruker.dittnav.eventhandler.common.exceptions.DuplicateEventException
import no.nav.personbruker.dittnav.eventhandler.common.exceptions.NoEventsException
import no.nav.personbruker.dittnav.eventhandler.config.innloggetBruker
import org.slf4j.LoggerFactory

fun Route.doneApi(doneEventService: DoneEventService) {

    val log = LoggerFactory.getLogger(DoneEventService::class.java)

    post("/produce/done") {
        respondForParameterType<Done> { doneDto ->
            try {
                doneEventService.markEventAsDone(innloggetBruker, doneDto)
                val msg = "Done-event er produsert. EventID: ${doneDto.eventId}. Uid: ${doneDto.uid}."
                log.info(msg)
                DoneResponse(msg, HttpStatusCode.OK)
            } catch (e: NoEventsException) {
                val msg = "Det ble ikke produsert et done-event fordi vi fant ikke eventet i cachen. EventId: ${doneDto.eventId}, Uid: ${doneDto.uid}."
                log.warn(msg, e)
                DoneResponse(msg, HttpStatusCode.NotFound)
            } catch (e: DuplicateEventException) {
                val msg = "Det ble ikke produsert done-event fordi det finnes duplikat av event. EventId: ${doneDto.eventId}, Uid: ${doneDto.uid}."
                log.error(msg, e)
                DoneResponse(msg, HttpStatusCode.NotModified)
            } catch (e: Exception) {
                val msg = "Done-event ble ikke produsert. EventID: ${doneDto.eventId}. Uid: ${doneDto.uid}."
                log.error(msg, e)
                DoneResponse(msg, HttpStatusCode.BadRequest)
            }
        }
    }
}

suspend inline fun <reified T : Any> PipelineContext<Unit, ApplicationCall>.respondForParameterType(handler: (T) -> DoneResponse) {
    val postParametersDto: T = call.receive()
    val message = handler.invoke(postParametersDto)
    call.respond(message.httpStatus, message)
}


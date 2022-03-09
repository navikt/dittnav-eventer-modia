package no.nav.personbruker.dittnav.eventer.modia.oppgave

import io.ktor.client.*
import no.nav.personbruker.dittnav.eventer.modia.common.AzureToken
import no.nav.personbruker.dittnav.eventer.modia.config.getWithAzureAndFnr
import java.net.URL

class OppgaveConsumer(
    private val client: HttpClient,
    eventHandlerBaseURL: URL
) {

    private val activeEventsEndpoint = URL("$eventHandlerBaseURL/fetch/modia/oppgave/aktive")
    private val inactiveEventsEndpoint = URL("$eventHandlerBaseURL/fetch/modia/oppgave/inaktive")
    private val allEventsEndpoint = URL("$eventHandlerBaseURL/fetch/modia/oppgave/all")

    suspend fun getActiveEvents(accessToken: AzureToken, fnr: String): List<Oppgave> {
        return getExternalEvents(accessToken, fnr, activeEventsEndpoint)
    }

    suspend fun getInactiveEvents(accessToken: AzureToken, fnr: String): List<Oppgave> {
        return getExternalEvents(accessToken, fnr, inactiveEventsEndpoint)
    }

    suspend fun getAllEvents(accessToken: AzureToken, fnr: String): List<Oppgave> {
        return getExternalEvents(accessToken, fnr, allEventsEndpoint)
    }

    private suspend fun getExternalEvents(accessToken: AzureToken, fnr: String, completePathToEndpoint: URL): List<Oppgave> {
        return client.getWithAzureAndFnr(completePathToEndpoint, accessToken, fnr)
    }
}

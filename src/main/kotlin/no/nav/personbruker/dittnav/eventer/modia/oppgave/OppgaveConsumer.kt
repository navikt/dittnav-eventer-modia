package no.nav.personbruker.dittnav.eventer.modia.oppgave

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.eventer.modia.common.AzureToken
import no.nav.personbruker.dittnav.eventer.modia.common.retryOnConnectionLost
import no.nav.personbruker.dittnav.eventer.modia.config.getWithAzureAndFnr
import java.net.URL

class OppgaveConsumer(
    private val client: HttpClient,
    eventHandlerBaseURL: URL
) {

    private val aktiveVarslerEndpoint = URL("$eventHandlerBaseURL/fetch/modia/oppgave/aktive")
    private val inaktiveVarslerEndpoint = URL("$eventHandlerBaseURL/fetch/modia/oppgave/inaktive")
    private val alleVarslerEndpoint = URL("$eventHandlerBaseURL/fetch/modia/oppgave/all")

    suspend fun getAktiveVarsler(accessToken: AzureToken, fnr: String): List<Oppgave> {
        return retryOnConnectionLost {
            getExternalVarsler(accessToken, fnr, aktiveVarslerEndpoint)
        }
    }

    suspend fun getInaktiveVarsler(accessToken: AzureToken, fnr: String): List<Oppgave> {
        return retryOnConnectionLost {
            getExternalVarsler(accessToken, fnr, inaktiveVarslerEndpoint)
        }
    }

    suspend fun getAlleVarsler(accessToken: AzureToken, fnr: String): List<Oppgave> {
        return retryOnConnectionLost {
            getExternalVarsler(accessToken, fnr, alleVarslerEndpoint)
        }
    }

    private suspend fun getExternalVarsler(
        accessToken: AzureToken,
        fnr: String,
        completePathToEndpoint: URL
    ): List<Oppgave> {
        return client.getWithAzureAndFnr(completePathToEndpoint, accessToken, fnr)
    }
}

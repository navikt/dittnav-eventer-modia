package no.nav.personbruker.dittnav.eventer.modia.beskjed

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.eventer.modia.common.AzureToken
import no.nav.personbruker.dittnav.eventer.modia.common.retryOnConnectionLost
import no.nav.personbruker.dittnav.eventer.modia.config.getWithAzureAndFnr
import java.net.URL

class BeskjedConsumer(
    private val client: HttpClient,
    varselHandlerBaseURL: URL
) {

    private val aktiveVarslerEndpoint = URL("$varselHandlerBaseURL/fetch/modia/beskjed/aktive")
    private val inaktiveVarslerEndpoint = URL("$varselHandlerBaseURL/fetch/modia/beskjed/inaktive")
    private val alleVarslerEndpoint = URL("$varselHandlerBaseURL/fetch/modia/beskjed/all")

    suspend fun getAktiveVarsler(accessToken: AzureToken, fnr: String): List<Beskjed> {
        return retryOnConnectionLost {
            getEksterneVarsler(accessToken, fnr, aktiveVarslerEndpoint)
        }
    }

    suspend fun getInaktiveVarsler(accessToken: AzureToken, fnr: String): List<Beskjed> {
        return retryOnConnectionLost {
            getEksterneVarsler(accessToken, fnr, inaktiveVarslerEndpoint)
        }
    }

    suspend fun getAlleVarsler(accessToken: AzureToken, fnr: String): List<Beskjed> {
        return retryOnConnectionLost {
            getEksterneVarsler(accessToken, fnr, alleVarslerEndpoint)
        }
    }

    private suspend fun getEksterneVarsler(accessToken: AzureToken, fnr: String, completePathToEndpoint: URL): List<Beskjed> {
        return client.getWithAzureAndFnr(completePathToEndpoint, accessToken, fnr)
    }
}

package no.nav.personbruker.dittnav.eventer.modia.innboks

import io.ktor.client.HttpClient
import no.nav.personbruker.dittnav.eventer.modia.common.AzureToken
import no.nav.personbruker.dittnav.eventer.modia.common.retryOnConnectionLost
import no.nav.personbruker.dittnav.eventer.modia.config.getWithAzureAndFnr
import java.net.URL

class InnboksConsumer(
    private val client: HttpClient,
    varselHandlerBaseURL: URL
) {

    private val aktiveVarslerEndpoint = URL("$varselHandlerBaseURL/fetch/modia/innboks/aktive")
    private val inaktiveVarslerEndpoint = URL("$varselHandlerBaseURL/fetch/modia/innboks/inaktive")
    private val alleVarslerEndpoint = URL("$varselHandlerBaseURL/fetch/modia/innboks/all")

    suspend fun getAktiveVarsler(accessToken: AzureToken, fnr: String): List<Innboks> {
        return retryOnConnectionLost {
            getExternalVarsler(accessToken, fnr, aktiveVarslerEndpoint)
        }
    }

    suspend fun getInaktiveVarsler(accessToken: AzureToken, fnr: String): List<Innboks> {
        return retryOnConnectionLost {
            getExternalVarsler(accessToken, fnr, inaktiveVarslerEndpoint)
        }
    }

    suspend fun getAlleVarsler(accessToken: AzureToken, fnr: String): List<Innboks> {
        return retryOnConnectionLost {
            getExternalVarsler(accessToken, fnr, alleVarslerEndpoint)
        }
    }

    private suspend fun getExternalVarsler(accessToken: AzureToken, fnr: String, completePathToEndpoint: URL): List<Innboks> {
        return client.getWithAzureAndFnr(completePathToEndpoint, accessToken, fnr)
    }
}

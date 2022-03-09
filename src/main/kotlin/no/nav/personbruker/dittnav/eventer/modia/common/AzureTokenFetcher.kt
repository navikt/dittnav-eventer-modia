package no.nav.personbruker.dittnav.eventer.modia.common

import no.nav.tms.token.support.azure.exchange.AzureService

class AzureTokenFetcher(private val azureService: AzureService, private val eventHandlerClientId: String) {

    suspend fun fetchTokenForEventHandler(): AzureToken {
        val tokenString = azureService.getAccessToken(eventHandlerClientId)

        return AzureToken(tokenString)
    }
}

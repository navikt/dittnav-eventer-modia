package no.nav.personbruker.dittnav.eventer.modia.innboks

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher

class InnboksEventService(
    private val innboksConsumer: InnboksConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun getActiveCachedEventsForUser(fnr: String): List<InnboksDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val innboksList = innboksConsumer.getActiveEvents(azureToken, fnr)

        return InnboksTransformer.toInnboksDTO(innboksList)
    }

    suspend fun getInactiveCachedEventsForUser(fnr: String): List<InnboksDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val innboksList = innboksConsumer.getInactiveEvents(azureToken, fnr)

        return InnboksTransformer.toInnboksDTO(innboksList)
    }

    suspend fun getAllCachedEventsForUser(fnr: String): List<InnboksDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val innboksList = innboksConsumer.getAllEvents(azureToken, fnr)

        return InnboksTransformer.toInnboksDTO(innboksList)
    }
}

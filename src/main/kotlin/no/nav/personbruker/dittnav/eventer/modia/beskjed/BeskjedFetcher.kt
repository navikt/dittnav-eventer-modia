package no.nav.personbruker.dittnav.eventer.modia.beskjed

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import no.nav.personbruker.dittnav.eventer.modia.common.User

class BeskjedFetcher(
    private val beskjedConsumer: BeskjedConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val beskjedList = beskjedConsumer.getActiveEvents(azureToken, bruker.fodselsnummer)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val beskjedList = beskjedConsumer.getInactiveEvents(azureToken, bruker.fodselsnummer)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val beskjedList = beskjedConsumer.getAllEvents(azureToken, bruker.fodselsnummer)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }
}

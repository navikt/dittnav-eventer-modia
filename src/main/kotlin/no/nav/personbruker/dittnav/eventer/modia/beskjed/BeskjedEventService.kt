package no.nav.personbruker.dittnav.eventer.modia.beskjed

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import no.nav.personbruker.dittnav.eventer.modia.common.User

class BeskjedEventService(
    private val beskjedConsumer: BeskjedConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<Beskjed> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return beskjedConsumer.getActiveEvents(azureToken, bruker.fodselsnummer)
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<Beskjed> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return beskjedConsumer.getInactiveEvents(azureToken, bruker.fodselsnummer)
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<Beskjed> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return beskjedConsumer.getAllEvents(azureToken, bruker.fodselsnummer)
    }
}

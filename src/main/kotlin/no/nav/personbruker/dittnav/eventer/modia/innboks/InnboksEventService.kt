package no.nav.personbruker.dittnav.eventer.modia.innboks

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import no.nav.personbruker.dittnav.eventer.modia.common.User

class InnboksEventService(
    private val innboksConsumer: InnboksConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<Innboks> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return innboksConsumer.getActiveEvents(azureToken, bruker.fodselsnummer)
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<Innboks> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return innboksConsumer.getInactiveEvents(azureToken, bruker.fodselsnummer)
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<Innboks> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return innboksConsumer.getAllEvents(azureToken, bruker.fodselsnummer)
    }
}

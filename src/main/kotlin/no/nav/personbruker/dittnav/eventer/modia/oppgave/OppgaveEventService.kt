package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import no.nav.personbruker.dittnav.eventer.modia.common.User

class OppgaveEventService(
    private val oppgaveConsumer: OppgaveConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<Oppgave> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return oppgaveConsumer.getActiveEvents(azureToken, bruker.fodselsnummer)
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<Oppgave> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return oppgaveConsumer.getInactiveEvents(azureToken, bruker.fodselsnummer)
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<Oppgave> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        return oppgaveConsumer.getAllEvents(azureToken, bruker.fodselsnummer)
    }
}

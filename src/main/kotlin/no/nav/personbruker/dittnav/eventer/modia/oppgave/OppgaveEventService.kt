package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher
import no.nav.personbruker.dittnav.eventer.modia.common.User

class OppgaveEventService(
    private val oppgaveConsumer: OppgaveConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getActiveEvents(azureToken, bruker.fodselsnummer)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getInactiveEvents(azureToken, bruker.fodselsnummer)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getAllEvents(azureToken, bruker.fodselsnummer)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }
}

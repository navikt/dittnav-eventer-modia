package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher

class OppgaveEventService(
    private val oppgaveConsumer: OppgaveConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun getActiveCachedEventsForUser(fnr: String): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getActiveEvents(azureToken, fnr)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }

    suspend fun getInactiveCachedEventsForUser(fnr: String): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getInactiveEvents(azureToken, fnr)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }

    suspend fun getAllCachedEventsForUser(fnr: String): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getAllEvents(azureToken, fnr)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }
}

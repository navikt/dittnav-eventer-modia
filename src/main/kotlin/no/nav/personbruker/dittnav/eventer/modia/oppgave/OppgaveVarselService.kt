package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher

class OppgaveVarselService(
    private val oppgaveConsumer: OppgaveConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun aktiveVarsler(fnr: String): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getAktiveVarsler(azureToken, fnr)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }

    suspend fun inaktiveVarsler(fnr: String): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getInaktiveVarsler(azureToken, fnr)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }

    suspend fun alleVarsler(fnr: String): List<OppgaveDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val oppgaveList = oppgaveConsumer.getAlleVarsler(azureToken, fnr)

        return OppgaveTransformer.toOppgaveDTO(oppgaveList)
    }
}

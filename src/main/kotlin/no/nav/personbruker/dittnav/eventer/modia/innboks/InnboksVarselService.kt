package no.nav.personbruker.dittnav.eventer.modia.innboks

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher

class InnboksVarselService(
    private val innboksConsumer: InnboksConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun aktiveVarsler(fnr: String): List<InnboksDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val innboksList = innboksConsumer.getAktiveVarsler(azureToken, fnr)

        return InnboksTransformer.toInnboksDTO(innboksList)
    }

    suspend fun inaktiveVarsler(fnr: String): List<InnboksDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val innboksList = innboksConsumer.getInaktiveVarsler(azureToken, fnr)

        return InnboksTransformer.toInnboksDTO(innboksList)
    }

    suspend fun alleVarsler(fnr: String): List<InnboksDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()

        val innboksList = innboksConsumer.getAlleVarsler(azureToken, fnr)

        return InnboksTransformer.toInnboksDTO(innboksList)
    }
}

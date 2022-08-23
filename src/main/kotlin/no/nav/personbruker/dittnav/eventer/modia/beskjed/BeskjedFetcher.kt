package no.nav.personbruker.dittnav.eventer.modia.beskjed

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher

class BeskjedFetcher(
    private val beskjedConsumer: BeskjedConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun aktiveVarsler(fnr: String): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()
        val beskjedList = beskjedConsumer.getActiveEvents(azureToken, fnr)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }

    suspend fun inaktiveVarsler(fnr: String): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()
        val beskjedList = beskjedConsumer.getInactiveEvents(azureToken, fnr)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }

    suspend fun alleVarsler(fnr: String): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForEventHandler()
        val beskjedList = beskjedConsumer.getAllEvents(azureToken, fnr)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }
}

package no.nav.personbruker.dittnav.eventer.modia.beskjed

import no.nav.personbruker.dittnav.eventer.modia.common.AzureTokenFetcher

class BeskjedVarselService(
    private val beskjedConsumer: BeskjedConsumer,
    private val azureTokenFetcher: AzureTokenFetcher
) {

    suspend fun aktiveVarsler(fnr: String): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForVarselHandler()
        val beskjedList = beskjedConsumer.getAktiveVarsler(azureToken, fnr)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }

    suspend fun inaktiveVarsler(fnr: String): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForVarselHandler()
        val beskjedList = beskjedConsumer.getInaktiveVarsler(azureToken, fnr)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }

    suspend fun alleVarsler(fnr: String): List<BeskjedDTO> {
        val azureToken = azureTokenFetcher.fetchTokenForVarselHandler()
        val beskjedList = beskjedConsumer.getAlleVarsler(azureToken, fnr)

        return BeskjedTransformer.toBeskjedDTO(beskjedList)
    }
}

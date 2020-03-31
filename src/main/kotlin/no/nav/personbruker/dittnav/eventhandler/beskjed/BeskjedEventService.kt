package no.nav.personbruker.dittnav.eventhandler.beskjed

import Beskjed
import no.nav.personbruker.dittnav.eventhandler.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventhandler.common.database.Database
import java.time.Instant
import java.time.ZoneId

class BeskjedEventService(
        private val database: Database
) {

    suspend fun getActiveCachedEventsForUser(bruker: InnloggetBruker): List<Beskjed> {
        return database.dbQuery {
            getAktivBeskjedForInnloggetBruker(bruker)
        }.filter { beskjed -> !beskjed.isExpired() }
    }

    suspend fun getInactiveCachedEventsForUser(bruker: InnloggetBruker): List<Beskjed> {
        val all = getAllEventsFromCacheForUser(bruker)
        val inactive = all.filter { beskjed -> !beskjed.aktiv }
        val expired = all.filter { beskjed -> beskjed.isExpired() }
        return inactive + expired
    }

    suspend fun getAllEventsFromCacheForUser(bruker: InnloggetBruker): List<Beskjed> {
        return database.dbQuery { getAllBeskjedForInnloggetBruker(bruker) }
    }

    private fun Beskjed.isExpired() : Boolean = synligFremTil?.isBefore(Instant.now().atZone(ZoneId.of("Europe/Oslo")))?: false
}

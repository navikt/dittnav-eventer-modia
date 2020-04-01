package no.nav.personbruker.dittnav.eventer.modia.beskjed

import Beskjed
import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database
import java.time.Instant
import java.time.ZoneId

class BeskjedEventService(
        private val database: Database
) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<Beskjed> {
        return database.dbQuery {
            getAktivBeskjedForInnloggetBruker(bruker)
        }.filter { beskjed -> !beskjed.isExpired() }
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<Beskjed> {
        val all = getAllEventsFromCacheForUser(bruker)
        val inactive = all.filter { beskjed -> !beskjed.aktiv }
        val expired = all.filter { beskjed -> beskjed.isExpired() }
        return inactive + expired
    }

    suspend fun getAllEventsFromCacheForUser(bruker: User): List<Beskjed> {
        return database.dbQuery { getAllBeskjedForInnloggetBruker(bruker) }
    }

    private fun Beskjed.isExpired() : Boolean = synligFremTil?.isBefore(Instant.now().atZone(ZoneId.of("Europe/Oslo")))?: false
}

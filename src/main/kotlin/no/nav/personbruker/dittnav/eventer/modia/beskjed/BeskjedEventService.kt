package no.nav.personbruker.dittnav.eventer.modia.beskjed

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.time.Instant
import java.time.ZoneId

class BeskjedEventService(
        private val database: Database
) {

    private val log = LoggerFactory.getLogger(BeskjedEventService::class.java)

    suspend fun getActiveCachedEventsForUser(bruker: User): List<BeskjedDTO> {
        return getEvents { getAktivBeskjedForInnloggetBruker(bruker) }
            .filter { beskjed -> !beskjed.isExpired() }
            .map { beskjed -> beskjed.toDTO() }
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<BeskjedDTO> {
        val all = getAllEventsFromCacheForUser(bruker)
        val inactive = all.filter { beskjed -> !beskjed.aktiv }.map { beskjed -> beskjed.toDTO() }
        val expired = all.filter { beskjed -> beskjed.isExpired() }.map { beskjed -> beskjed.toDTO() }
        return inactive + expired
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<BeskjedDTO> {
        return getAllEventsFromCacheForUser(bruker)
            .map { beskjed -> beskjed.toDTO() }
    }

    private suspend fun getAllEventsFromCacheForUser(bruker: User): List<Beskjed> {
        return getEvents { getAllBeskjedForInnloggetBruker(bruker) }
    }

    private fun Beskjed.isExpired() : Boolean = synligFremTil?.isBefore(Instant.now().atZone(ZoneId.of("Europe/Oslo")))?: false

    private suspend fun getEvents(operationToExecute: Connection.() -> List<Beskjed>): List<Beskjed> {
        val events = database.queryWithExceptionTranslation {
            operationToExecute()
        }
        if(produsentIsEmpty(events)) {
            log.warn("Returnerer beskjed-eventer med tom produsent til frontend. Kanskje er ikke systembrukeren lagt inn i systembruker-tabellen?")
        }
        return events
    }

    private fun produsentIsEmpty(events: List<Beskjed>): Boolean {
        return events.any { beskjed -> beskjed.produsent.isNullOrEmpty() }
    }
}

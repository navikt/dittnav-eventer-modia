package no.nav.personbruker.dittnav.eventer.modia.innboks

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database
import org.slf4j.LoggerFactory
import java.sql.Connection

class InnboksEventService(private val database: Database) {

    private val log = LoggerFactory.getLogger(InnboksEventService::class.java)

    suspend fun getActiveCachedEventsForUser(bruker: User): List<Innboks> {
        return getEvents { getAktivInnboksForInnloggetBruker(bruker) }
    }

    suspend fun getInctiveCachedEventsForUser(bruker: User): List<Innboks> {
        return getEvents { getAktivInnboksForInnloggetBruker(bruker) }
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<Innboks> {
        return getEvents { getAllInnboksForInnloggetBruker(bruker) }
    }

    private suspend fun getEvents(operationToExecute: Connection.() -> List<Innboks>): List<Innboks> {
        val events = database.queryWithExceptionTranslation {
            operationToExecute()
        }
        if(produsentIsEmpty(events)) {
            log.warn("Returnerer innboks-eventer med tom produsent til frontend. Kanskje er ikke systembrukeren lagt inn i systembruker-tabellen?")
        }
        return events
    }

    private fun produsentIsEmpty(events: List<Innboks>): Boolean {
        return events.any { innboks -> innboks.produsent.isNullOrEmpty() }
    }
}

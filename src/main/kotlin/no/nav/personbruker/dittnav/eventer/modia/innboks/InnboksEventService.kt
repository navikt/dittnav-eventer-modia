package no.nav.personbruker.dittnav.eventer.modia.innboks

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database
import org.slf4j.LoggerFactory
import java.sql.Connection

class InnboksEventService(private val database: Database) {

    private val log = LoggerFactory.getLogger(InnboksEventService::class.java)

    suspend fun getActiveCachedEventsForUser(bruker: User): List<InnboksDTO> {
        return getEvents { getAktivInnboksForInnloggetBruker(bruker) }
            .map { innboks -> innboks.toDTO() }
    }

    suspend fun getInctiveCachedEventsForUser(bruker: User): List<InnboksDTO> {
        return getEvents { getInaktivInnboksForInnloggetBruker(bruker) }
            .map { innboks -> innboks.toDTO() }
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<InnboksDTO> {
        return getEvents { getAllInnboksForInnloggetBruker(bruker) }
            .map { innboks -> innboks.toDTO() }
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

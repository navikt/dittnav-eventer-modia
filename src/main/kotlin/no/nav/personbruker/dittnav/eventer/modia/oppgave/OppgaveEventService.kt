package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database
import org.slf4j.LoggerFactory
import java.sql.Connection

class OppgaveEventService(
        private val database: Database
) {

    private val log = LoggerFactory.getLogger(OppgaveEventService::class.java)

    suspend fun getActiveCachedEventsForUser(bruker: User): List<OppgaveDTO> {
        return getEvents { getAktivOppgaveForInnloggetBruker(bruker) }
            .map { oppgave -> oppgave.toDTO() }
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<OppgaveDTO> {
        return getEvents { getInaktivOppgaveForInnloggetBruker(bruker) }
            .map { oppgave -> oppgave.toDTO() }
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<OppgaveDTO> {
        return getEvents { getAllOppgaveForInnloggetBruker(bruker) }
            .map { oppgave -> oppgave.toDTO() }
    }

    private suspend fun getEvents(operationToExecute: Connection.() -> List<Oppgave>): List<Oppgave> {
        val events = database.queryWithExceptionTranslation {
            operationToExecute()
        }
        if(produsentIsEmpty(events)) {
            log.warn("Returnerer oppgave-eventer med tom produsent til frontend. Kanskje er ikke systembrukeren lagt inn i systembruker-tabellen?")
        }
        return events
    }

    private fun produsentIsEmpty(events: List<Oppgave>): Boolean {
        return events.any { oppgave -> oppgave.produsent.isNullOrEmpty() }
    }
}

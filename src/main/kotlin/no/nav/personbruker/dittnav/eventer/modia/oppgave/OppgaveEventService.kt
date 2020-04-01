package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database

class OppgaveEventService(
        private val database: Database
) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<Oppgave> {
        return database.dbQuery { getAktivOppgaveForInnloggetBruker(bruker) }
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<Oppgave> {
        return database.dbQuery { getInaktivOppgaveForInnloggetBruker(bruker) }
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<Oppgave> {
        return database.dbQuery { getAllOppgaveForInnloggetBruker(bruker) }
    }
}

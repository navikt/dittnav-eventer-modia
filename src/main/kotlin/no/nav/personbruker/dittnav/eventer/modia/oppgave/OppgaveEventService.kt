package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.InnloggetBruker
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database

class OppgaveEventService(
        private val database: Database
) {

    suspend fun getActiveCachedEventsForUser(bruker: InnloggetBruker): List<Oppgave> {
        return database.dbQuery { getAktivOppgaveForInnloggetBruker(bruker) }
    }

    suspend fun getInactiveCachedEventsForUser(bruker: InnloggetBruker): List<Oppgave> {
        return database.dbQuery { getInaktivOppgaveForInnloggetBruker(bruker) }
    }

    suspend fun getAllCachedEventsForUser(bruker: InnloggetBruker): List<Oppgave> {
        return database.dbQuery { getAllOppgaveForInnloggetBruker(bruker) }
    }
}

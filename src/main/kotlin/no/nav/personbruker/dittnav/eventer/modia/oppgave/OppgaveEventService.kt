package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database

class OppgaveEventService(
        private val database: Database
) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<Oppgave> {
        return database.queryWithExceptionTranslation { getAktivOppgaveForInnloggetBruker(bruker) }
    }

    suspend fun getInactiveCachedEventsForUser(bruker: User): List<Oppgave> {
        return database.queryWithExceptionTranslation { getInaktivOppgaveForInnloggetBruker(bruker) }
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<Oppgave> {
        return database.queryWithExceptionTranslation { getAllOppgaveForInnloggetBruker(bruker) }
    }
}

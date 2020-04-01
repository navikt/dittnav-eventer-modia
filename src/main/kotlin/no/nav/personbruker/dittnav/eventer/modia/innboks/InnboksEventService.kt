package no.nav.personbruker.dittnav.eventer.modia.innboks

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database

class InnboksEventService(private val database: Database) {

    suspend fun getActiveCachedEventsForUser(bruker: User): List<Innboks> {
        return database.dbQuery { getAktivInnboksForInnloggetBruker(bruker) }
    }

    suspend fun getInctiveCachedEventsForUser(bruker: User): List<Innboks> {
        return database.dbQuery { getAktivInnboksForInnloggetBruker(bruker) }
    }

    suspend fun getAllCachedEventsForUser(bruker: User): List<Innboks> {
        return database.dbQuery { getAllInnboksForInnloggetBruker(bruker) }
    }
}

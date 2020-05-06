package no.nav.personbruker.dittnav.eventer.modia.common.exceptions.database

import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.EventCacheException

class UnretriableDatabaseException(message: String, cause: Throwable?) : EventCacheException(message, cause) {
    constructor(message: String) : this(message, null)
}

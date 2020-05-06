package no.nav.personbruker.dittnav.eventer.modia.common.exceptions.kafka

import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.EventCacheException

class NoEventsException(message: String, cause: Throwable?) : EventCacheException(message, cause) {
    constructor(message: String) : this(message, null)
}

package no.nav.personbruker.dittnav.eventer.modia.config

import kotlinx.serialization.json.Json

fun jsonConfig(): Json {
    return Json {
        this.ignoreUnknownKeys = true
        this.encodeDefaults = true
    }
}

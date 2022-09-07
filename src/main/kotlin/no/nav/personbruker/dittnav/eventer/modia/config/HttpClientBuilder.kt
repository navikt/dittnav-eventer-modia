package no.nav.personbruker.dittnav.eventer.modia.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

object HttpClientBuilder {

    fun build(): HttpClient {
        return HttpClient(Apache) {
            install(ContentNegotiation) {
                jsonConfig()
            }
            install(HttpTimeout)
        }
    }
}

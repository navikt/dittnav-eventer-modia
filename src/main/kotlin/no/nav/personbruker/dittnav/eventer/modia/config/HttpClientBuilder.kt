package no.nav.personbruker.dittnav.eventer.modia.config

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.serializer.*

object HttpClientBuilder {

    fun build(jsonSerializer: KotlinxSerializer = KotlinxSerializer(jsonConfig())): HttpClient {
        return HttpClient(Apache) {
            install(JsonFeature) {
                serializer = jsonSerializer
            }
            install(HttpTimeout)
        }
    }
}

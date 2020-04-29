package no.nav.personbruker.dittnav.eventer.modia.common

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.response.respondTextWriter
import io.ktor.routing.Route
import io.ktor.routing.get
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedEventService
import no.nav.personbruker.dittnav.eventer.modia.beskjed.getFirstBeskjed
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.time.Duration
import java.time.LocalDateTime

fun Route.healthApi(database: Database, collectorRegistry: CollectorRegistry = CollectorRegistry.defaultRegistry) {

    val pingJsonResponse = """{"ping": "pong"}"""

    get("/isAlive") {
        call.respondText(text = "ALIVE", contentType = ContentType.Text.Plain)
    }

    get("/isReady") {
        if (isDataSourceRunning(database) && isDBSchemaUpdated(database)) {
            call.respondText(text = "READY", contentType = ContentType.Text.Plain)

        } else {
            call.respondText(text = "NOTREADY", contentType = ContentType.Text.Plain, status = HttpStatusCode.FailedDependency)
        }
    }

    get("/ping") {
        call.respondText(pingJsonResponse, ContentType.Application.Json)
    }

    get("/metrics") {
        val names = call.request.queryParameters.getAll("name[]")?.toSet() ?: emptySet()
        call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
            TextFormat.write004(this, collectorRegistry.filteredMetricFamilySamples(names))
        }
    }
}

val log = LoggerFactory.getLogger(BeskjedEventService::class.java)
var lastDBSchemaCheck = LocalDateTime.now()
var dbSchemaReady = false

private fun isDBSchemaUpdated(database: Database): Boolean {
    return if(Math.abs(Duration.between(lastDBSchemaCheck, LocalDateTime.now()).toSeconds()) > 20) {
        runBlocking {
            lastDBSchemaCheck = LocalDateTime.now()
            try {
                database.dbQuery { getFirstBeskjed() }
                log.info("Database-skjema er oppdatert!")
                dbSchemaReady = true
                dbSchemaReady
            } catch (e: Exception) {
                log.info("Database-skjema er ikke oppdatert, ikke ready")
                dbSchemaReady;
            }
        }
    } else {
        dbSchemaReady
    }
}

fun isDataSourceRunning(database: Database): Boolean {
    return database.dataSource.isRunning
}

package no.nav.personbruker.dittnav.eventer.modia.beskjed

import Beskjed
import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.map
import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getInaktivBeskjedForInnloggetBruker(bruker: User): List<Beskjed> =
        getBeskjedForInnloggetBruker(bruker, false)

fun Connection.getAktivBeskjedForInnloggetBruker(bruker: User): List<Beskjed> =
        getBeskjedForInnloggetBruker(bruker, true)

fun Connection.getFirstBeskjed(): List<Beskjed> =
        prepareStatement("""SELECT * FROM BESKJED LIMIT 1""")
                .use { it.executeQuery().map { toBeskjed() } }

fun Connection.getAllBeskjedForInnloggetBruker(bruker: User): List<Beskjed> =
        prepareStatement("""SELECT 
            |beskjed.id, 
            |beskjed.uid, 
            |beskjed.eventTidspunkt,
            |beskjed.fodselsnummer,
            |beskjed.eventId, 
            |beskjed.grupperingsId,
            |beskjed.tekst,
            |beskjed.link,
            |beskjed.sikkerhetsnivaa,
            |beskjed.sistOppdatert,
            |beskjed.synligFremTil,
            |beskjed.aktiv,
            |beskjed.systembruker,
            |systembrukere.produsentnavn AS produsent
            |FROM beskjed INNER JOIN systembrukere ON beskjed.systembruker = systembrukere.systembruker
            |WHERE beskjed.fodselsnummer = ?""".trimMargin())
                .use {
                    it.setString(1, bruker.fodselsnummer)
                    it.executeQuery().map {
                        toBeskjed()
                    }
                }

fun ResultSet.toBeskjed(): Beskjed {
    return Beskjed(
            id = getInt("id"),
            uid = getString("uid"),
            produsent = getString("produsent"),
            systembruker = getString("systembruker"),
            eventTidspunkt = ZonedDateTime.ofInstant(getTimestamp("eventTidspunkt").toInstant(), ZoneId.of("Europe/Oslo")),
            fodselsnummer = getString("fodselsnummer"),
            eventId = getString("eventId"),
            grupperingsId = getString("grupperingsId"),
            tekst = getString("tekst"),
            link = getString("link"),
            sikkerhetsnivaa = getInt("sikkerhetsnivaa"),
            sistOppdatert = ZonedDateTime.ofInstant(getTimestamp("sistOppdatert").toInstant(), ZoneId.of("Europe/Oslo")),
            synligFremTil = getNullableZonedDateTime("synligFremTil"),
            aktiv = getBoolean("aktiv")
    )
}

private fun Connection.getBeskjedForInnloggetBruker(bruker: User, aktiv: Boolean): List<Beskjed> =
        prepareStatement("""SELECT
            |beskjed.id, 
            |beskjed.uid, 
            |beskjed.eventTidspunkt,
            |beskjed.fodselsnummer,
            |beskjed.eventId, 
            |beskjed.grupperingsId,
            |beskjed.tekst,
            |beskjed.link,
            |beskjed.sikkerhetsnivaa,
            |beskjed.sistOppdatert,
            |beskjed.synligFremTil,
            |beskjed.aktiv,
            |beskjed.systembruker,
            |systembrukere.produsentnavn AS produsent
            |FROM beskjed INNER JOIN systembrukere ON beskjed.systembruker = systembrukere.systembruker
            |WHERE beskjed.fodselsnummer = ? AND beskjed.aktiv = ?""".trimMargin())
                .use {
                    it.setString(1, bruker.fodselsnummer)
                    it.setBoolean(2, aktiv)
                    it.executeQuery().map {
                        toBeskjed()
                    }
                }

private fun ResultSet.getNullableZonedDateTime(label: String) : ZonedDateTime? {
    return getTimestamp(label)?.let { timestamp -> ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.of("Europe/Oslo")) }
}

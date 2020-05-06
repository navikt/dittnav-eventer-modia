package no.nav.personbruker.dittnav.eventer.modia.innboks

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.map
import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getInaktivInnboksForInnloggetBruker(bruker: User): List<Innboks> =
        getInnboksForInnloggetBruker(bruker, false)

fun Connection.getAktivInnboksForInnloggetBruker(bruker: User): List<Innboks> =
        getInnboksForInnloggetBruker(bruker, true)

fun Connection.getAllInnboksForInnloggetBruker(bruker: User): List<Innboks> =
        prepareStatement("""SELECT
            |innboks.id,
            |innboks.eventTidspunkt,
            |innboks.fodselsnummer,
            |innboks.eventId,
            |innboks.grupperingsId,
            |innboks.tekst,
            |innboks.link,
            |innboks.sikkerhetsnivaa,
            |innboks.sistOppdatert,
            |innboks.aktiv,
            |innboks.systembruker,
            |systembrukere.produsentnavn AS produsent
            |FROM innboks LEFT JOIN systembrukere ON innboks.systembruker = systembrukere.systembruker
            |WHERE innboks.fodselsnummer = ?""".trimMargin())
                .use {
                    it.setString(1, bruker.fodselsnummer)
                    it.executeQuery().map {
                        toInnboks()
                    }
                }

private fun Connection.getInnboksForInnloggetBruker(bruker: User, aktiv: Boolean): List<Innboks> =
        prepareStatement("""SELECT
            |innboks.id,
            |innboks.eventTidspunkt,
            |innboks.fodselsnummer,
            |innboks.eventId,
            |innboks.grupperingsId,
            |innboks.tekst,
            |innboks.link,
            |innboks.sikkerhetsnivaa,
            |innboks.sistOppdatert,
            |innboks.aktiv,
            |innboks.systembruker,
            |systembrukere.produsentnavn AS produsent
            |FROM innboks LEFT JOIN systembrukere ON innboks.systembruker = systembrukere.systembruker
            |WHERE fodselsnummer = ? AND aktiv = ?""".trimMargin())
                .use {
                    it.setString(1, bruker.fodselsnummer)
                    it.setBoolean(2, aktiv)
                    it.executeQuery().map {
                        toInnboks()
                    }
                }

private fun ResultSet.toInnboks(): Innboks {
    return Innboks(
            id = getInt("id"),
            produsent = getString("produsent") ?: "",
            systembruker = getString("systembruker"),
            eventTidspunkt = ZonedDateTime.ofInstant(getTimestamp("eventTidspunkt").toInstant(), ZoneId.of("Europe/Oslo")),
            fodselsnummer = getString("fodselsnummer"),
            eventId = getString("eventId"),
            grupperingsId = getString("grupperingsId"),
            tekst = getString("tekst"),
            link = getString("link"),
            sikkerhetsnivaa = getInt("sikkerhetsnivaa"),
            sistOppdatert = ZonedDateTime.ofInstant(getTimestamp("sistOppdatert").toInstant(), ZoneId.of("Europe/Oslo")),
            aktiv = getBoolean("aktiv")
    )
}

package no.nav.personbruker.dittnav.eventer.modia.oppgave

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.map
import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getInaktivOppgaveForInnloggetBruker(bruker: User): List<Oppgave> =
        getOppgaveForInnloggetBruker(bruker, false)

fun Connection.getAktivOppgaveForInnloggetBruker(bruker: User): List<Oppgave> =
        getOppgaveForInnloggetBruker(bruker, true)

fun Connection.getAllOppgaveForInnloggetBruker(bruker: User): List<Oppgave> =
        prepareStatement("""SELECT 
            |oppgave.id,
            |oppgave.eventTidspunkt,
            |oppgave.fodselsnummer,
            |oppgave.eventId,
            |oppgave.grupperingsId,
            |oppgave.tekst,
            |oppgave.link,
            |oppgave.sikkerhetsnivaa,
            |oppgave.sistOppdatert,
            |oppgave.aktiv,
            |oppgave.systembruker,
            |systembrukere.produsentnavn AS produsent
            |FROM oppgave LEFT JOIN systembrukere ON oppgave.systembruker = systembrukere.systembruker
            |WHERE fodselsnummer = ?""".trimMargin())
                .use {
                    it.setString(1, bruker.fodselsnummer)
                    it.executeQuery().map {
                        toOppgave()
                    }
                }



private fun Connection.getOppgaveForInnloggetBruker(bruker: User, aktiv: Boolean): List<Oppgave> =
        prepareStatement("""SELECT
            |oppgave.id,
            |oppgave.eventTidspunkt,
            |oppgave.fodselsnummer,
            |oppgave.eventId,
            |oppgave.grupperingsId,
            |oppgave.tekst,
            |oppgave.link,
            |oppgave.sikkerhetsnivaa,
            |oppgave.sistOppdatert,
            |oppgave.aktiv,
            |oppgave.systembruker,
            |systembrukere.produsentnavn AS produsent
            |FROM oppgave LEFT JOIN systembrukere ON oppgave.systembruker = systembrukere.systembruker
            |WHERE fodselsnummer = ? AND aktiv = ?""".trimMargin())
                .use {
                    it.setString(1, bruker.fodselsnummer)
                    it.setBoolean(2, aktiv)
                    it.executeQuery().map {
                        toOppgave()
                    }
                }

private fun ResultSet.toOppgave(): Oppgave {
    return Oppgave(
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

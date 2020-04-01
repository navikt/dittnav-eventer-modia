package no.nav.personbruker.dittnav.eventer.modia.innboks

import no.nav.personbruker.dittnav.eventer.modia.common.User
import no.nav.personbruker.dittnav.eventer.modia.common.database.map
import java.sql.Connection
import java.sql.ResultSet
import java.time.ZoneId
import java.time.ZonedDateTime

fun Connection.getAllInnboksForInnloggetBruker(bruker: User): List<Innboks> =
        prepareStatement("""SELECT * FROM INNBOKS WHERE fodselsnummer = ?""")
            .use {
                it.setString(1, bruker.ident)
                it.executeQuery().map {
                    toInnboks()
                }
            }

fun Connection.getInaktivInnboksForInnloggetBruker(bruker: User): List<Innboks> =
        getInnboksForInnloggetBruker(bruker, false)

fun Connection.getAktivInnboksForInnloggetBruker(bruker: User): List<Innboks> =
        getInnboksForInnloggetBruker(bruker, true)

private fun Connection.getInnboksForInnloggetBruker(bruker: User, aktiv: Boolean): List<Innboks> =
        prepareStatement("""SELECT * FROM INNBOKS WHERE fodselsnummer = ? AND aktiv = ?""")
            .use {
                it.setString(1, bruker.ident)
                it.setBoolean(2, aktiv)
                it.executeQuery().map {
                    toInnboks()
                }
            }

private fun ResultSet.toInnboks(): Innboks {
    return Innboks(
            id = getInt("id"),
            produsent = getString("produsent"),
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

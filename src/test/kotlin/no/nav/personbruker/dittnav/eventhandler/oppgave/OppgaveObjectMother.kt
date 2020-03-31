package no.nav.personbruker.dittnav.eventhandler.oppgave

import java.time.ZoneId
import java.time.ZonedDateTime

fun createOppgave(id: Int, eventId: String, fodselsnummer: String, aktiv: Boolean): Oppgave {
    return Oppgave(
            id = id,
            fodselsnummer = fodselsnummer,
            grupperingsId = "100$fodselsnummer",
            eventId = eventId,
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            produsent = "DittNav",
            sikkerhetsnivaa = 4,
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            tekst = "Dette er melding til brukeren",
            link = "https://nav.no/systemX/$fodselsnummer",
            aktiv = aktiv)
}

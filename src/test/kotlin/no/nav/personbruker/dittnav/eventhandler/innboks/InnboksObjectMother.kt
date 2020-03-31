package no.nav.personbruker.dittnav.eventhandler.innboks

import java.time.ZoneId
import java.time.ZonedDateTime

fun createInnboks(id: Int, eventId: String, fodselsnummer: String, aktiv: Boolean): Innboks {
    return Innboks(
            id = id,
            produsent = "DittNav",
            eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            fodselsnummer = fodselsnummer,
            eventId = eventId,
            grupperingsId = "100$fodselsnummer",
            tekst = "Dette er melding til brukeren",
            link = "https://nav.no/systemX/$fodselsnummer",
            sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
            sikkerhetsnivaa = 4,
            aktiv = aktiv)
}

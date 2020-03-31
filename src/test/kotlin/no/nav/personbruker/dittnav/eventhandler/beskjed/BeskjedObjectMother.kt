package no.nav.personbruker.dittnav.eventhandler.beskjed;

import Beskjed
import java.time.ZoneId
import java.time.ZonedDateTime

fun createBeskjed(id: Int, eventId: String, fodselsnummer: String, synligFremTil: ZonedDateTime?, uid: String, aktiv: Boolean): Beskjed {
        return Beskjed(
                uid = uid,
                id = id,
                produsent = "DittNav",
                eventTidspunkt = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                fodselsnummer = fodselsnummer,
                eventId = eventId,
                grupperingsId = "100$fodselsnummer",
                tekst = "Dette er beskjed til brukeren",
                link = "https://nav.no/systemX/$fodselsnummer",
                sistOppdatert = ZonedDateTime.now(ZoneId.of("Europe/Oslo")),
                synligFremTil = synligFremTil,
                sikkerhetsnivaa = 4,
                aktiv = aktiv)
        }

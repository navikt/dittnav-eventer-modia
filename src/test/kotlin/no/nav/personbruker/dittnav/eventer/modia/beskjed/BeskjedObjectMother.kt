package no.nav.personbruker.dittnav.eventer.modia.beskjed;

import java.time.ZoneId
import java.time.ZonedDateTime

object BeskjedObjectMother {
        fun createBeskjed(eventId: String, fodselsnummer: String, synligFremTil: ZonedDateTime?, uid: String, aktiv: Boolean): Beskjed {
                return Beskjed(
                        uid = uid,
                        produsent = "dittnav",
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
}

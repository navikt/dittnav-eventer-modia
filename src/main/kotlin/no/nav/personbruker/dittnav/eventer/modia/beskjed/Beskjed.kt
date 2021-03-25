package no.nav.personbruker.dittnav.eventer.modia.beskjed

import java.time.ZonedDateTime

data class Beskjed(
        val id: Int,
        val uid: String,
        val fodselsnummer: String,
        val grupperingsId: String,
        val eventId: String,
        val eventTidspunkt: ZonedDateTime,
        val produsent: String,
        val systembruker: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: ZonedDateTime,
        val synligFremTil: ZonedDateTime?,
        val tekst: String,
        val link: String,
        val aktiv: Boolean
) {
    override fun toString(): String {
        return "Beskjed(" +
                "id=$id, " +
                "uid=$uid, " +
                "fodselsnummer=***, " +
                "grupperingsId=$grupperingsId, " +
                "eventId=$eventId, " +
                "eventTidspunkt=$eventTidspunkt, " +
                "produsent=$produsent, " +
                "systembruker=***, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "synligFremTil=$synligFremTil, " +
                "tekst=***, " +
                "link=***, " +
                "aktiv=$aktiv"
    }
}

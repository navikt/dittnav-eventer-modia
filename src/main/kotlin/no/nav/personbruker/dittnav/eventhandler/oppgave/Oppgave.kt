package no.nav.personbruker.dittnav.eventhandler.oppgave

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.ZonedDateTime

data class Oppgave(
        @JsonIgnore val id: Int?,
        val fodselsnummer: String,
        val grupperingsId: String,
        val eventId: String,
        val eventTidspunkt: ZonedDateTime,
        @JsonIgnore val produsent: String,
        val sikkerhetsnivaa: Int,
        val sistOppdatert: ZonedDateTime,
        val tekst: String,
        val link: String,
        val aktiv: Boolean
) {
    override fun toString(): String {
        return "Oppgave(" +
                "id=$id, " +
                "fodselsnummer=***, " +
                "grupperingsId=$grupperingsId, " +
                "eventId=$eventId, " +
                "eventTidspunkt=$eventTidspunkt, " +
                "produsent=$produsent, " +
                "sikkerhetsnivaa=$sikkerhetsnivaa, " +
                "sistOppdatert=$sistOppdatert, " +
                "tekst=***, " +
                "link=***, " +
                "aktiv=$aktiv"
    }
}

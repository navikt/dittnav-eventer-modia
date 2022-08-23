@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.eventer.modia.beskjed

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.eventer.modia.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class BeskjedDTO(
    val fodselsnummer: String,
    val grupperingsId: String,
    val eventId: String,
    val forstBehandlet: ZonedDateTime,
    val produsent: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: ZonedDateTime,
    val synligFremTil: ZonedDateTime? = null,
    val tekst: String,
    val link: String,
    val aktiv: Boolean
)
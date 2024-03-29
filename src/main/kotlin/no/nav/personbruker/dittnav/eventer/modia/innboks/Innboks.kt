@file:UseSerializers(ZonedDateTimeSerializer::class)
package no.nav.personbruker.dittnav.eventer.modia.innboks

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.personbruker.dittnav.eventer.modia.common.EksternVarsling
import no.nav.personbruker.dittnav.eventer.modia.common.serializer.ZonedDateTimeSerializer
import java.time.ZonedDateTime

@Serializable
data class Innboks(
    val produsent: String,
    val forstBehandlet: ZonedDateTime,
    val fodselsnummer: String,
    val eventId: String,
    val grupperingsId: String,
    val tekst: String,
    val link: String,
    val sikkerhetsnivaa: Int,
    val sistOppdatert: ZonedDateTime,
    val aktiv: Boolean,
    val eksternVarsling: EksternVarsling? = null
) {
    val eksternVarslingSendt = eksternVarsling?.sendt ?: false
    val eksternVarslingKanaler = eksternVarsling?.sendteKanaler ?: emptyList()
}

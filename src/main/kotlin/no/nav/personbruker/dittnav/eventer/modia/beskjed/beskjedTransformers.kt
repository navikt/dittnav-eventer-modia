package no.nav.personbruker.dittnav.eventer.modia.beskjed

fun Beskjed.toDTO() = BeskjedDTO(
    uid = uid,
    fodselsnummer = fodselsnummer,
    grupperingsId = grupperingsId,
    eventId = eventId,
    eventTidspunkt = eventTidspunkt,
    produsent = produsent,
    sikkerhetsnivaa = sikkerhetsnivaa,
    sistOppdatert = sistOppdatert,
    synligFremTil = synligFremTil,
    tekst = tekst,
    link = link,
    aktiv = aktiv
)

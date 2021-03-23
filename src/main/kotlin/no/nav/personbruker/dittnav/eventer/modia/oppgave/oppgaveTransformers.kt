package no.nav.personbruker.dittnav.eventer.modia.oppgave

fun Oppgave.toDTO() = OppgaveDTO(
    fodselsnummer = fodselsnummer,
    grupperingsId = grupperingsId,
    eventId = eventId,
    eventTidspunkt = eventTidspunkt,
    produsent = produsent,
    sikkerhetsnivaa = sikkerhetsnivaa,
    sistOppdatert = sistOppdatert,
    tekst = tekst,
    link = link,
    aktiv = aktiv
)

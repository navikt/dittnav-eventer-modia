package no.nav.personbruker.dittnav.eventer.modia.config

import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedEventService
import no.nav.personbruker.dittnav.eventer.modia.common.database.Database
import no.nav.personbruker.dittnav.eventer.modia.done.DoneEventService
import no.nav.personbruker.dittnav.eventer.modia.done.DoneProducer
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksEventService
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveEventService

class ApplicationContext {

    val environment = Environment()
    val database: Database = PostgresDatabase(environment)
    val beskjedEventService = BeskjedEventService(database)
    val oppgaveEventService = OppgaveEventService(database)
    val innboksEventService = InnboksEventService(database)
    val doneProducer = DoneProducer(environment)
    val doneEventService = DoneEventService(database, doneProducer)
}

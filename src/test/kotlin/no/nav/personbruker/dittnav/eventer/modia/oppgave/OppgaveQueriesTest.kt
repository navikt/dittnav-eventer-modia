package no.nav.personbruker.dittnav.eventer.modia.oppgave

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventer.modia.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventer.modia.common.database.H2Database
import no.nav.personbruker.dittnav.eventer.modia.common.database.createProdusent
import no.nav.personbruker.dittnav.eventer.modia.common.database.deleteProdusent
import no.nav.personbruker.dittnav.eventhandler.oppgave.createOppgave
import no.nav.personbruker.dittnav.eventhandler.oppgave.deleteOppgave
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OppgaveQueriesTest {

    private val database = H2Database()

    private val bruker = InnloggetBrukerObjectMother.createInnloggetBruker("12345")

    private val oppgave1 = OppgaveObjectMother.createOppgave(id = 1, eventId = "123", fodselsnummer = "12345", aktiv = true)
    private val oppgave2 = OppgaveObjectMother.createOppgave(id = 2, eventId = "345", fodselsnummer = "12345", aktiv = true)
    private val oppgave3 = OppgaveObjectMother.createOppgave(id = 3, eventId = "567", fodselsnummer = "12345", aktiv = false)
    private val oppgave4 = OppgaveObjectMother.createOppgave(id = 4, eventId = "789", fodselsnummer = "54321", aktiv = true)

    @BeforeAll
    fun `populer testdata`() {
        runBlocking {
            database.dbQuery { createOppgave(listOf(oppgave1, oppgave2, oppgave3, oppgave4)) }
            database.dbQuery { createProdusent(systembruker = "x-dittnav", produsentnavn = "dittnav") }
        }
    }

    @AfterAll
    fun `slett testdata`() {
        runBlocking {
            database.dbQuery { deleteOppgave(listOf(oppgave1, oppgave2, oppgave3, oppgave4)) }
            database.dbQuery { deleteProdusent(systembruker = "x-dittnav") }
        }
    }

    @Test
    fun `Finn alle cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllOppgaveForInnloggetBruker(bruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finn kun aktive cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAktivOppgaveForInnloggetBruker(bruker) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Finn kun inaktive cachede Oppgave-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getInaktivOppgaveForInnloggetBruker(bruker) }.size `should be equal to` 1
        }
    }

    @Test
    fun `Returnerer tom liste hvis Oppgave-eventer for fodselsnummer ikke finnes`() {
        val brukerSomIkkeFinnes = InnloggetBrukerObjectMother.createInnloggetBruker("0")
        runBlocking {
            database.dbQuery { getAktivOppgaveForInnloggetBruker(brukerSomIkkeFinnes) }.isEmpty()
        }
    }

    @Test
    fun `Returnerer tom liste hvis fodselsnummer er tomt`() {
        val fodselsnummerMangler = InnloggetBrukerObjectMother.createInnloggetBruker("")
        runBlocking {
            database.dbQuery { getAktivOppgaveForInnloggetBruker(fodselsnummerMangler) }.isEmpty()
        }
    }
}

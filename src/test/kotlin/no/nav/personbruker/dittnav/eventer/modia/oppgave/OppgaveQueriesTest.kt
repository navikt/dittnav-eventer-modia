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
        createOppgave(listOf(oppgave1, oppgave2, oppgave3, oppgave4))
        createSystembruker(systembruker = "x-dittnav", produsentnavn = "dittnav")
    }

    @AfterAll
    fun `slett testdata`() {
        deleteOppgave(listOf(oppgave1, oppgave2, oppgave3, oppgave4))
        deleteSystembruker(systembruker = "x-dittnav")
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

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for aktive eventer`() {
        runBlocking {
            val oppgave = database.dbQuery { getAktivOppgaveForInnloggetBruker(bruker) }.first()
            oppgave.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for inaktive eventer`() {
        runBlocking {
            val oppgave = database.dbQuery { getInaktivOppgaveForInnloggetBruker(bruker) }.first()
            oppgave.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for alle eventer`() {
        runBlocking {
            val oppgave = database.dbQuery { getAllOppgaveForInnloggetBruker(bruker) }.first()
            oppgave.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer tom streng for produsent hvis eventet er produsert av systembruker vi ikke har i systembruker-tabellen`() {
        var oppgaveMedAnnenProdusent = OppgaveObjectMother.createOppgave(id = 5, eventId = "111", fodselsnummer = "112233", aktiv = true)
                .copy(systembruker = "ukjent-systembruker")
        createOppgave(listOf(oppgaveMedAnnenProdusent))
        val oppgave = runBlocking {
            database.dbQuery {
                getAllOppgaveForInnloggetBruker(InnloggetBrukerObjectMother.createInnloggetBruker("112233"))
            }.first()
        }
        oppgave.produsent `should be equal to` ""
        deleteOppgave(listOf(oppgaveMedAnnenProdusent))
    }

    private fun createOppgave(oppgaver: List<Oppgave>) {
        runBlocking {
            database.dbQuery { createOppgave(oppgaver) }
        }
    }

    private fun createSystembruker(systembruker: String, produsentnavn: String) {
        runBlocking {
            database.dbQuery { createProdusent(systembruker, produsentnavn) }
        }
    }

    private fun deleteOppgave(oppgaver: List<Oppgave>) {
        runBlocking {
            database.dbQuery { deleteOppgave(oppgaver) }
        }
    }

    private fun deleteSystembruker(systembruker: String) {
        runBlocking {
            database.dbQuery { deleteProdusent(systembruker) }
        }
    }
}

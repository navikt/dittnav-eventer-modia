package no.nav.personbruker.dittnav.eventer.modia.innboks

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventer.modia.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventer.modia.common.database.H2Database
import no.nav.personbruker.dittnav.eventhandler.innboks.createInnboks
import no.nav.personbruker.dittnav.eventhandler.innboks.deleteInnboks
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InnboksQueriesTest {

    private val database = H2Database()

    private val bruker1 = InnloggetBrukerObjectMother.createInnloggetBruker("12345")
    private val bruker2 = InnloggetBrukerObjectMother.createInnloggetBruker("67890")

    private val innboks1 = InnboksObjectMother.createInnboks(id = 1, eventId = "123", fodselsnummer = "12345", aktiv = true)
    private val innboks2 = InnboksObjectMother.createInnboks(id = 2, eventId = "345", fodselsnummer = "12345", aktiv = true)
    private val innboks3 = InnboksObjectMother.createInnboks(id = 3, eventId = "567", fodselsnummer = "67890", aktiv = true)
    private val innboks4 = InnboksObjectMother.createInnboks(id = 4, eventId = "789", fodselsnummer = "67890", aktiv = false)

    @BeforeAll
    fun `populer tabellen med Innboks-eventer`() {
        runBlocking {
            database.dbQuery { createInnboks(listOf(innboks1, innboks2, innboks3, innboks4)) }
        }
    }

    @AfterAll
    fun `slett Innboks-eventer fra tabellen`() {
        runBlocking {
            database.dbQuery { deleteInnboks(listOf(innboks1, innboks2, innboks3, innboks4)) }
        }

    }

    @Test
    fun `Finn alle cachede Innboks-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllInnboksForInnloggetBruker(bruker1) }.size `should be equal to` 2
            database.dbQuery { getAllInnboksForInnloggetBruker(bruker2) }.size `should be equal to` 2
        }
    }

    @Test
    fun `Finn kun aktive cachede Innboks-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAktivInnboksForInnloggetBruker(bruker1) }.size `should be equal to` 2
            database.dbQuery { getAktivInnboksForInnloggetBruker(bruker2) }.size `should be equal to` 1
        }
    }

    @Test
    fun `Finn kun inaktive cachede Innboks-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getInaktivInnboksForInnloggetBruker(bruker1) }.`should be empty`()
            database.dbQuery { getInaktivInnboksForInnloggetBruker(bruker2) }.size `should be equal to` 1
        }
    }

    @Test
    fun `Returnerer tom liste hvis Innboks-eventer for fodselsnummer ikke finnes`() {
        val brukerUtenEventer = InnloggetBrukerObjectMother.createInnloggetBruker("0")
        runBlocking {
            database.dbQuery { getAllInnboksForInnloggetBruker(brukerUtenEventer) }.size `should be equal to` 0
        }
    }

    @Test
    fun `Returnerer tom liste hvis fodselsnummer er tomt`() {
        val brukerUtenEventer = InnloggetBrukerObjectMother.createInnloggetBruker("")
        runBlocking {
            database.dbQuery { getAllInnboksForInnloggetBruker(brukerUtenEventer) }.size `should be equal to` 0
        }
    }
}

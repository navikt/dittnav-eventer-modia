package no.nav.personbruker.dittnav.eventhandler.innboks

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventhandler.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventhandler.common.database.H2Database
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class InnboksQueriesTest {

    private val database = H2Database()

    private val bruker1 = InnloggetBrukerObjectMother.createInnloggetBruker("12345")
    private val bruker2 = InnloggetBrukerObjectMother.createInnloggetBruker("67890")

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

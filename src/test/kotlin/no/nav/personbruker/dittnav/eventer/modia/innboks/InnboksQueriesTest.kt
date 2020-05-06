package no.nav.personbruker.dittnav.eventer.modia.innboks

import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventer.modia.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventer.modia.common.database.H2Database
import no.nav.personbruker.dittnav.eventer.modia.common.database.createProdusent
import no.nav.personbruker.dittnav.eventer.modia.common.database.deleteProdusent
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
    fun `populer testdata`() {
        createInnboks(listOf(innboks1, innboks2, innboks3, innboks4))
        createSystembruker(systembruker = "x-dittnav", produsentnavn = "dittnav")
    }

    @AfterAll
    fun `slett testdata`() {
        deleteInnboks(listOf(innboks1, innboks2, innboks3, innboks4))
        deleteSystembruker(systembruker = "x-dittnav")
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

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for aktive eventer`() {
        runBlocking {
            val innboks = database.dbQuery { getAktivInnboksForInnloggetBruker(bruker1) }.first()
            innboks.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for inaktive eventer`() {
        runBlocking {
            val innboks = database.dbQuery { getInaktivInnboksForInnloggetBruker(bruker2) }.first()
            innboks.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for alle eventer`() {
        runBlocking {
            val innboks = database.dbQuery { getAllInnboksForInnloggetBruker(bruker1) }.first()
            innboks.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer tom streng for produsent hvis eventet er produsert av systembruker vi ikke har i systembruker-tabellen`() {
        var innboksMedAnnenProdusent = InnboksObjectMother.createInnboks(id = 5, eventId = "111", fodselsnummer = "112233", aktiv = true)
                .copy(systembruker = "ukjent-systembruker")
        createInnboks(listOf(innboksMedAnnenProdusent))
        val innboks = runBlocking {
            database.dbQuery {
                getAllInnboksForInnloggetBruker(InnloggetBrukerObjectMother.createInnloggetBruker("112233"))
            }.first()
        }
        innboks.produsent `should be equal to` ""
        deleteInnboks(listOf(innboksMedAnnenProdusent))
    }

    private fun createInnboks(innboks: List<Innboks>) {
        runBlocking {
            database.dbQuery { createInnboks(innboks) }
        }
    }

    private fun createSystembruker(systembruker: String, produsentnavn: String) {
        runBlocking {
            database.dbQuery { createProdusent(systembruker, produsentnavn) }
        }
    }

    private fun deleteInnboks(innboks: List<Innboks>) {
        runBlocking {
            database.dbQuery { deleteInnboks(innboks) }
        }
    }

    private fun deleteSystembruker(systembruker: String) {
        runBlocking {
            database.dbQuery { deleteProdusent(systembruker) }
        }
    }
}

package no.nav.personbruker.dittnav.eventer.modia.beskjed

import Beskjed
import kotlinx.coroutines.runBlocking
import no.nav.personbruker.dittnav.eventer.modia.common.InnloggetBrukerObjectMother
import no.nav.personbruker.dittnav.eventer.modia.common.database.H2Database
import no.nav.personbruker.dittnav.eventer.modia.common.database.createProdusent
import no.nav.personbruker.dittnav.eventer.modia.common.database.deleteProdusent
import no.nav.personbruker.dittnav.eventhandler.beskjed.createBeskjed
import no.nav.personbruker.dittnav.eventhandler.beskjed.deleteBeskjed
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.ZonedDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BeskjedQueriesTest {

    private val database = H2Database()

    private val bruker = InnloggetBrukerObjectMother.createInnloggetBruker("12345")

    private val beskjed1 = BeskjedObjectMother.createBeskjed(id = 1, eventId = "123", fodselsnummer = "12345",
            synligFremTil = ZonedDateTime.now().plusHours(1), uid = "11", aktiv = true)
    private val beskjed2 = BeskjedObjectMother.createBeskjed(id = 2, eventId = "345", fodselsnummer = "12345",
            synligFremTil = ZonedDateTime.now().plusHours(1), uid = "22", aktiv = true)
    private val beskjed3 = BeskjedObjectMother.createBeskjed(id = 3, eventId = "567", fodselsnummer = "12345",
            synligFremTil = ZonedDateTime.now().plusHours(1), uid = "33", aktiv = false)
    private val beskjed4 = BeskjedObjectMother.createBeskjed(id = 4, eventId = "789", fodselsnummer = "54321",
            synligFremTil = ZonedDateTime.now().plusHours(1), uid = "44", aktiv = true)

    @BeforeAll
    fun `populer testdata`() {
        createBeskjed(listOf(beskjed1, beskjed2, beskjed3, beskjed4))
        createSystembruker(systembruker = "x-dittnav", produsentnavn = "dittnav")
    }

    @AfterAll
    fun `slett testdata`() {
        deleteBeskjed(listOf(beskjed1, beskjed2, beskjed3, beskjed4))
        deleteSystembruker(systembruker = "x-dittnav")
    }

    @Test
    fun `Finn alle cachede Beskjed-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery { getAllBeskjedForInnloggetBruker(bruker) }.size `should be equal to` 3
        }
    }

    @Test
    fun `Finn kun aktive cachede Beskjed-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery {
                val aktivBeskjedByUser = getAktivBeskjedForInnloggetBruker(bruker)
                aktivBeskjedByUser
            }.size `should be equal to` 2
        }
    }

    @Test
    fun `Finn kun inaktive cachede Beskjed-eventer for fodselsnummer`() {
        runBlocking {
            database.dbQuery {
                val inaktivBeskjedByUser = getInaktivBeskjedForInnloggetBruker(bruker)
                inaktivBeskjedByUser
            }.size `should be equal to` 1
        }
    }

    @Test
    fun `Returnerer tom liste hvis Beskjed-eventer for fodselsnummer ikke finnes`() {
        val brukerSomIkkeFinnes = InnloggetBrukerObjectMother.createInnloggetBruker("0")
        runBlocking {
            database.dbQuery { getAktivBeskjedForInnloggetBruker(brukerSomIkkeFinnes) }.`should be empty`()
        }
    }

    @Test
    fun `Returnerer tom liste hvis fodselsnummer er tomt`() {
        val fodselsnummerMangler = InnloggetBrukerObjectMother.createInnloggetBruker("")
        runBlocking {
            database.dbQuery { getAktivBeskjedForInnloggetBruker(fodselsnummerMangler) }.`should be empty`()
        }
    }

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for aktive eventer`() {
        runBlocking {
            val beskjed = database.dbQuery { getAktivBeskjedForInnloggetBruker(bruker) }.first()
            beskjed.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for inaktive eventer`() {
        runBlocking {
            val beskjed = database.dbQuery { getInaktivBeskjedForInnloggetBruker(bruker) }.first()
            beskjed.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer lesbart navn for produsent som kan eksponeres for alle eventer`() {
        runBlocking {
            val beskjed = database.dbQuery { getAllBeskjedForInnloggetBruker(bruker) }.first()
            beskjed.produsent `should be equal to` "dittnav"
        }
    }

    @Test
    fun `Returnerer tom streng for produsent hvis eventet er produsert av systembruker vi ikke har i systembruker-tabellen`() {
        var beskjedMedAnnenProdusent = BeskjedObjectMother.createBeskjed(id = 5, eventId = "111", fodselsnummer = "112233",
                synligFremTil = ZonedDateTime.now().plusHours(1), uid = "11", aktiv = true)
                .copy(systembruker = "ukjent-systembruker")
        createBeskjed(listOf(beskjedMedAnnenProdusent))
        val beskjed = runBlocking {
            database.dbQuery {
                getAllBeskjedForInnloggetBruker(InnloggetBrukerObjectMother.createInnloggetBruker("112233"))
            }.first()
        }
        beskjed.produsent `should be equal to` ""
        deleteBeskjed(listOf(beskjedMedAnnenProdusent))
    }

    private fun createBeskjed(beskjeder: List<Beskjed>) {
        runBlocking {
            database.dbQuery { createBeskjed(beskjeder) }
        }
    }

    private fun createSystembruker(systembruker: String, produsentnavn: String) {
        runBlocking {
            database.dbQuery { createProdusent(systembruker, produsentnavn) }
        }
    }

    private fun deleteBeskjed(beskjeder: List<Beskjed>) {
        runBlocking {
            database.dbQuery { deleteBeskjed(beskjeder) }
        }
    }

    private fun deleteSystembruker(systembruker: String) {
        runBlocking {
            database.dbQuery { deleteProdusent(systembruker) }
        }
    }

}

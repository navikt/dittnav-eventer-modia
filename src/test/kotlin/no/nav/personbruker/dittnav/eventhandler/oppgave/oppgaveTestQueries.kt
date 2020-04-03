package no.nav.personbruker.dittnav.eventhandler.oppgave

import no.nav.personbruker.dittnav.eventer.modia.oppgave.Oppgave
import java.sql.Connection
import java.sql.Types

fun Connection.createOppgave(oppgaver: List<Oppgave>) =
        prepareStatement("""INSERT INTO oppgave(id, produsent, eventTidspunkt, fodselsnummer, eventId, grupperingsId, tekst, link, sikkerhetsnivaa, sistOppdatert, aktiv)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""")
                .use { pStatement ->
                    oppgaver.forEach { oppgave ->
                        pStatement.setInt(1, oppgave.id)
                        pStatement.setString(2, oppgave.produsent)
                        pStatement.setObject(3, oppgave.eventTidspunkt.toLocalDateTime(), Types.TIMESTAMP)
                        pStatement.setString(4, oppgave.fodselsnummer)
                        pStatement.setString(5, oppgave.eventId)
                        pStatement.setString(6, oppgave.grupperingsId)
                        pStatement.setString(7, oppgave.tekst)
                        pStatement.setString(8, oppgave.link)
                        pStatement.setInt(9, oppgave.sikkerhetsnivaa)
                        pStatement.setObject(10, oppgave.sistOppdatert.toLocalDateTime(), Types.TIMESTAMP)
                        pStatement.setBoolean(11, oppgave.aktiv)
                        pStatement.addBatch()
                    }
                    pStatement.executeBatch()
                }

fun Connection.deleteOppgave(oppgaver: List<Oppgave>) =
        prepareStatement("""DELETE FROM oppgave WHERE eventId = ?""")
                .use { pStatement ->
                    oppgaver.forEach { oppgave ->
                        pStatement.setString(1, oppgave.eventId)
                        pStatement.addBatch()
                    }
                    pStatement.executeBatch()
                }

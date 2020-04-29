package no.nav.personbruker.dittnav.eventhandler.beskjed

import Beskjed
import java.sql.Connection
import java.sql.Types

fun Connection.createBeskjed(beskjeder: List<Beskjed>) =
        prepareStatement("""INSERT INTO beskjed(id, systembruker, eventTidspunkt, fodselsnummer, eventId, grupperingsId, tekst, link, sikkerhetsnivaa, sistOppdatert, aktiv, synligFremTil, uid)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""")
                .use { pStatement ->
                    beskjeder.forEach { beskjed ->
                        pStatement.setInt(1, beskjed.id)
                        pStatement.setString(2, beskjed.systembruker)
                        pStatement.setObject(3, beskjed.eventTidspunkt.toLocalDateTime(), Types.TIMESTAMP)
                        pStatement.setString(4, beskjed.fodselsnummer)
                        pStatement.setString(5, beskjed.eventId)
                        pStatement.setString(6, beskjed.grupperingsId)
                        pStatement.setString(7, beskjed.tekst)
                        pStatement.setString(8, beskjed.link)
                        pStatement.setInt(9, beskjed.sikkerhetsnivaa)
                        pStatement.setObject(10, beskjed.sistOppdatert.toLocalDateTime(), Types.TIMESTAMP)
                        pStatement.setBoolean(11, beskjed.aktiv)
                        pStatement.setObject(12, beskjed.synligFremTil?.toLocalDateTime(), Types.TIMESTAMP)
                        pStatement.setString(13, beskjed.uid)
                        pStatement.addBatch()
                    }
                    pStatement.executeBatch()
                }

fun Connection.deleteBeskjed(beskjeder: List<Beskjed>) =
        prepareStatement("""DELETE FROM beskjed WHERE eventId = ?""")
                .use { pStatement ->
                    beskjeder.forEach { beskjed ->
                        pStatement.setString(1, beskjed.eventId)
                        pStatement.addBatch()
                    }
                    pStatement.executeBatch()
                }

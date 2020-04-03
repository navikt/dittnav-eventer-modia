package no.nav.personbruker.dittnav.eventhandler.beskjed

import Beskjed
import java.sql.Connection
import java.sql.Types

fun Connection.createBeskjed(beskjed: List<Beskjed>) =
        prepareStatement("""INSERT INTO beskjed(id, produsent, eventTidspunkt, fodselsnummer, eventId, grupperingsId, tekst, link, sikkerhetsnivaa, sistOppdatert, aktiv, synligFremTil, uid)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""")
                .use {
                    beskjed.forEach { i ->
                        run {
                            it.setInt(1, i.id)
                            it.setString(2, i.produsent)
                            it.setObject(3, i.eventTidspunkt.toLocalDateTime(), Types.TIMESTAMP)
                            it.setString(4, i.fodselsnummer)
                            it.setString(5, i.eventId)
                            it.setString(6, i.grupperingsId)
                            it.setString(7, i.tekst)
                            it.setString(8, i.link)
                            it.setInt(9, i.sikkerhetsnivaa)
                            it.setObject(10, i.sistOppdatert.toLocalDateTime(), Types.TIMESTAMP)
                            it.setBoolean(11, i.aktiv)
                            it.setObject(12, i.synligFremTil?.toLocalDateTime(), Types.TIMESTAMP)
                            it.setString(13, i.uid)
                            it.addBatch()
                        }
                    }
                    it.executeBatch()
                }

fun Connection.deleteBeskjed(beskjed: List<Beskjed>) =
        prepareStatement("""DELETE FROM beskjed WHERE eventId = ?""")
                .use {
                    beskjed.forEach { i ->
                        run {
                            it.setString(1, i.eventId)
                            it.addBatch()
                        }
                    }
                    it.executeBatch()
                }

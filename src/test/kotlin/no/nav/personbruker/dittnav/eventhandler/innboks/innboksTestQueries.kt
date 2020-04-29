package no.nav.personbruker.dittnav.eventhandler.innboks

import no.nav.personbruker.dittnav.eventer.modia.innboks.Innboks
import java.sql.Connection
import java.sql.Types

fun Connection.createInnboks(innbokseventer: List<Innboks>) =
        prepareStatement("""INSERT INTO innboks(id, systembruker, eventTidspunkt, fodselsnummer, eventId, grupperingsId, tekst, link, sikkerhetsnivaa, sistOppdatert, aktiv)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""")
                .use { pStatement ->
                    innbokseventer.forEach { innboks ->
                        pStatement.setInt(1, innboks.id)
                        pStatement.setString(2, innboks.systembruker)
                        pStatement.setObject(3, innboks.eventTidspunkt.toLocalDateTime(), Types.TIMESTAMP)
                        pStatement.setString(4, innboks.fodselsnummer)
                        pStatement.setString(5, innboks.eventId)
                        pStatement.setString(6, innboks.grupperingsId)
                        pStatement.setString(7, innboks.tekst)
                        pStatement.setString(8, innboks.link)
                        pStatement.setInt(9, innboks.sikkerhetsnivaa)
                        pStatement.setObject(10, innboks.sistOppdatert.toLocalDateTime(), Types.TIMESTAMP)
                        pStatement.setBoolean(11, innboks.aktiv)
                        pStatement.addBatch()
                    }
                    pStatement.executeBatch()
                }

fun Connection.deleteInnboks(innbokseventer: List<Innboks>) =
        prepareStatement("""DELETE FROM innboks WHERE eventId = ?""")
                .use { pStatement ->
                    innbokseventer.forEach { innboks ->
                        pStatement.setString(1, innboks.eventId)
                        pStatement.addBatch()
                    }
                    pStatement.executeBatch()
                }

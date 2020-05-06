package no.nav.personbruker.dittnav.eventer.modia.common.database

import java.sql.Connection

fun Connection.createProdusent(systembruker: String, produsentnavn: String) =
        prepareStatement("""INSERT INTO systembrukere(systembruker, produsentnavn) VALUES(?, ?)""")
                .use {
                    it.setString(1, systembruker)
                    it.setString(2, produsentnavn)
                    it.executeUpdate()
                }

fun Connection.deleteProdusent(systembruker: String) =
        prepareStatement("""DELETE FROM systembrukere WHERE systembruker = ?""")
                .use {
                    it.setString(1, systembruker)
                    it.executeUpdate()
                }

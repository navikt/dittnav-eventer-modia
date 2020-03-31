package no.nav.personbruker.dittnav.eventer.modia.done

import Beskjed
import no.nav.personbruker.dittnav.eventer.modia.beskjed.toBeskjed
import no.nav.personbruker.dittnav.eventer.modia.common.database.map
import java.sql.Connection

fun Connection.getActiveBeskjedByIds(fodselsnummer: String, uid: String, eventId: String): List<Beskjed> =
        prepareStatement("""SELECT * FROM BESKJED WHERE fodselsnummer = ? AND uid = ? AND eventId = ? AND aktiv = true""")
                .use {
                    it.setString(1, fodselsnummer)
                    it.setString(2, uid)
                    it.setString(3, eventId)
                    it.executeQuery().map {
                        toBeskjed()
                    }
                }
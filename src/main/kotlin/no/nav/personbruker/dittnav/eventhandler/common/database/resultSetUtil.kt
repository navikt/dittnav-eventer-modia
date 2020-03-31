package no.nav.personbruker.dittnav.eventhandler.common.database

import java.sql.ResultSet

fun <T> ResultSet.map(result: ResultSet.() -> T): List<T> =
        mutableListOf<T>().apply {
            while (next()) {
                add(result())
            }
        }
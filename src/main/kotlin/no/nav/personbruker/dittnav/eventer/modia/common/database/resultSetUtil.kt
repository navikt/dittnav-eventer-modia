package no.nav.personbruker.dittnav.eventer.modia.common.database

import java.sql.ResultSet

fun <T> ResultSet.map(result: ResultSet.() -> T): List<T> =
        mutableListOf<T>().apply {
            while (next()) {
                add(result())
            }
        }
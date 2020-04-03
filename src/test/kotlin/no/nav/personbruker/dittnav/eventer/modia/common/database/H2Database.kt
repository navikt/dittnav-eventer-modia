package no.nav.personbruker.dittnav.eventer.modia.common.database

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.runBlocking

class H2Database : Database {

    private val memDataSource: HikariDataSource

    init {
        memDataSource = createDataSource()
        createTablesAndViews()
    }

    override val dataSource: HikariDataSource
        get() = memDataSource

    private fun createDataSource(): HikariDataSource {
        return HikariDataSource().apply {
            jdbcUrl = "jdbc:h2:mem:testdb"
            username = "sa"
            password = ""
            validate()
        }
    }

    private fun createTablesAndViews() {
        runBlocking {
            val fileContent = this::class.java.getResource("/db/createTablesAndViews.sql").readText()
            dbQuery { prepareStatement(fileContent).execute() }
        }
    }
}

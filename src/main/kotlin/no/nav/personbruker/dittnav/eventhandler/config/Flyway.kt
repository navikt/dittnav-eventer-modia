package no.nav.personbruker.dittnav.eventhandler.config

import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import javax.sql.DataSource

object Flyway {

    fun runFlywayMigrations(env: Environment) {
        val flyway = configure(env).load()
        flyway.migrate()
    }

    private fun configure(env: Environment): FluentConfiguration {
        val configBuilder = Flyway.configure()
        val dataSource = createCorrectAdminDatasourceForEnvironment(env)
        configBuilder.dataSource(dataSource)
        return configBuilder
    }

    private fun createCorrectAdminDatasourceForEnvironment(env: Environment): DataSource {
        return when (ConfigUtil.isCurrentlyRunningOnNais()) {
            true -> createDataSourceViaVaultWithAdminUser(env)
            false -> createDataSourceForLocalDbWithAdminUser(env)
        }
    }

    private fun createDataSourceViaVaultWithAdminUser(env: Environment): HikariDataSource {
        return PostgresDatabase.hikariDatasourceViaVault(env, env.dbReadOnlyUser)
    }

    private fun createDataSourceForLocalDbWithAdminUser(env: Environment): HikariDataSource {
        return PostgresDatabase.hikariFromLocalDb(env, env.dbUser)
    }

}
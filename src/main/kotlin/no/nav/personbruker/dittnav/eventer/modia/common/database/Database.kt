package no.nav.personbruker.dittnav.eventer.modia.common.database

import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.database.RetriableDatabaseException
import no.nav.personbruker.dittnav.eventer.modia.common.exceptions.database.UnretriableDatabaseException
import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.SQLException
import java.sql.SQLRecoverableException
import java.sql.SQLTransientException

interface Database {

    val dataSource: HikariDataSource

    suspend fun <T> dbQuery(operationToExecute: Connection.() -> T): T = withContext(Dispatchers.IO) {
        dataSource.connection.use { openConnection ->
            try {
                openConnection.operationToExecute().apply {
                    openConnection.commit()
                }

            } catch (e: Exception) {
                try {
                    openConnection.rollback()
                } catch (rollbackException: Exception) {
                    e.addSuppressed(rollbackException)
                }
                throw e
            }
        }
    }

    suspend fun <T> queryWithExceptionTranslation(operationToExecute: Connection.() -> T): T {
        return translateExternalExceptionsToInternalOnes {
            dbQuery {
                operationToExecute()
            }
        }
    }

}

inline fun <T> translateExternalExceptionsToInternalOnes(databaseActions: () -> T): T {
    return try {
        databaseActions()

    } catch (te: SQLTransientException) {
        val message = "Lesing fra databasen feilet grunnet en periodisk feil."
        throw RetriableDatabaseException(message, te)

    } catch (re: SQLRecoverableException) {
        val message = "Lesing fra databasen feilet grunnet en periodisk feil."
        throw RetriableDatabaseException(message, re)

    } catch (pe: PSQLException) {
        val message = "Det skjedde en SQL relatert feil ved lesing fra databasen."
        val ure = UnretriableDatabaseException(message, pe)
        pe.sqlState?.map { sqlState -> ure.addContext("sqlState", sqlState) }
        throw ure

    } catch (se: SQLException) {
        val message = "Det skjedde en SQL relatert feil ved lesing fra databasen."
        val ure = UnretriableDatabaseException(message, se)
        se.sqlState?.map { sqlState -> ure.addContext("sqlState", sqlState) }
        throw ure

    } catch (e: Exception) {
        val message = "Det skjedde en ukjent feil ved lesing fra databasen."
        throw UnretriableDatabaseException(message, e)
    }
}

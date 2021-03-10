package no.nav.personbruker.dittnav.eventer.modia.config

import java.net.URL

data class Environment(val dbHost: String = getEnvVar("DB_HOST"),
                       val dbName: String = getEnvVar("DB_NAME"),
                       val dbUser: String = getEnvVar("DB_NAME") + "-user",
                       val dbReadOnlyUser: String = getEnvVar("DB_NAME") + "-readonly",
                       val dbUrl: String = "jdbc:postgresql://$dbHost/$dbName",
                       val dbMountPath: String = getEnvVar("DB_MOUNT_PATH"),
                       val issoJwksUrl: URL = URL(getEnvVar("ISSO_JWKS_URL")),
                       val issoIssuer: String = getEnvVar("ISSO_ISSUER"),
                       val issoAcceptedAudience: List<String> = getEnvVar("ISSO_ACCEPTED_AUDIENCE")
                           .split(",")
                           .map(String::trim)
)

fun getEnvVar(varName: String): String {
    return System.getenv(varName)
            ?: throw IllegalArgumentException("Appen kan ikke starte uten av miljøvariabelen $varName er satt.")
}

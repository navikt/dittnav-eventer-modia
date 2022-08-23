package no.nav.personbruker.dittnav.eventer.modia.config

import java.net.URL

data class Environment(
    val issoJwksUrl: URL = URL(getEnvVar("ISSO_JWKS_URL")),
    val issoIssuer: String = getEnvVar("ISSO_ISSUER"),
    val issoAcceptedAudience: List<String> = getEnvVar("ISSO_ACCEPTED_AUDIENCE")
        .split(",")
        .map(String::trim),
    val eventHandlerUrl: String = getEnvVar("EVENT_HANDLER_URL"),
    val eventHandlerClientId: String = getEnvVar("EVENT_HANDLER_CLIENT_ID")
)

fun getEnvVar(varName: String): String {
    return System.getenv(varName)
        ?: throw IllegalArgumentException("Appen kan ikke starte uten av miljøvariabelen $varName er satt.")
}

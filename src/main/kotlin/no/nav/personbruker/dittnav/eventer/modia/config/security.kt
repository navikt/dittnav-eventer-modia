package no.nav.personbruker.dittnav.eventer.modia.config

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.auth.parseAuthorizationHeader
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import java.net.URL
import java.util.concurrent.TimeUnit

internal fun issoAuthenticationBuilder(environment: Environment): Application.() -> Unit = {
    install(Authentication) {
        jwt { setupIssoAuthentication(environment) }
    }
}

fun JWTAuthenticationProvider.Config.setupIssoAuthentication(environment: Environment) {
     authHeader(Security::useTokenFromCookie)
    val jwkProvider = Security.initJwkProvider(environment.issoJwksUrl)
    verifier(jwkProvider, environment.issoIssuer)
    realm = "dittnav-eventer-modia"
    validate { credentials ->
        return@validate Security.validationLogicPerRequest(credentials, environment)
    }
}

object Security {

    private const val cookieName = "ID_token"

    fun useTokenFromCookie(call: ApplicationCall): HttpAuthHeader? {
        return try {
            val tokenFromCookie = call.request.cookies[cookieName]
            val tokenAsAuthHeader = "Bearer $tokenFromCookie"
            parseAuthorizationHeader(tokenAsAuthHeader)
        } catch (t: Throwable) {
            log.warn("Det skjedde en feil ved forsøket på å hente ut et token fra cookie-en '$cookieName'", t)
            null
        }
    }

    fun initJwkProvider(securityJwksUri: URL): JwkProvider {
        return JwkProviderBuilder(securityJwksUri)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    }

    fun validationLogicPerRequest(credentials: JWTCredential, environment: Environment): JWTPrincipal? {
        return when (isCorrectAudienceSet(credentials, environment)) {
            true -> JWTPrincipal(credentials.payload)
            false -> null
        }
    }

    private fun isCorrectAudienceSet(credentials: JWTCredential, environment: Environment) =
        environment.issoAcceptedAudience.any {
            credentials.payload.audience.contains(it)
        }
}

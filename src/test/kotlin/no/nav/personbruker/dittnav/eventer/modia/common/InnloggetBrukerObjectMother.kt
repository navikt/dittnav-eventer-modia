package no.nav.personbruker.dittnav.eventer.modia.common

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.security.Key

object InnloggetBrukerObjectMother {

    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun createInnloggetBruker(): User {
        val ident = "12345"
        return createInnloggetBruker(ident)
    }

    fun createInnloggetBruker(ident: String): User {
        val innloggingsnivaa = 4
        return createInnloggetBruker(ident, innloggingsnivaa)
    }

    fun createInnloggetBruker(ident: String, innloggingsnivaa: Int): User {
        val jws = Jwts.builder()
                .setSubject(ident)
                .addClaims(mutableMapOf(Pair("acr", "Level$innloggingsnivaa")) as Map<String, Any>?)
                .signWith(key).compact()
        return User(ident)
    }

}

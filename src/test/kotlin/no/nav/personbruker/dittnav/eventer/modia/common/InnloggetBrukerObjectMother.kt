package no.nav.personbruker.dittnav.eventer.modia.common

object InnloggetBrukerObjectMother {

    fun createInnloggetBruker(): User {
        val ident = "12345"
        return createInnloggetBruker(ident)
    }

    fun createInnloggetBruker(ident: String): User {
        return User(ident)
    }

}

package no.nav.personbruker.dittnav.eventer.modia.common

import java.util.Locale

enum class IdentityClaim(val claimName: String) {

    SUBJECT("sub"),
    PID("pid");

    companion object {
        fun fromClaimName(claimName: String): IdentityClaim {
            values().forEach { currentClaim ->
                if (currentClaim.claimName == claimName.lowercase(Locale.getDefault())) {
                    return currentClaim
                }
            }
            val msg = "Ugyldig claim name '$claimName', gyldige verdier er ${values().toSet()}"
            throw IllegalArgumentException(msg)
        }
    }

    override fun toString(): String {
        return "IdentidyClaim(claimName='$claimName')"
    }
}

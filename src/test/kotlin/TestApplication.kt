import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.Application
import io.mockk.mockk
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedVarselService
import no.nav.personbruker.dittnav.eventer.modia.config.api
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksVarselService
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveEventService
import no.nav.tms.token.support.authentication.installer.mock.installMockedAuthenticators
import no.nav.tms.token.support.tokenx.validation.mock.SecurityLevel
internal val objectmapper = ObjectMapper()
fun mockApi(
    authConfig: Application.() -> Unit = mockAuthBuilder(),
    oppgaveEventService: OppgaveEventService = mockk(relaxed = true),
    innboksVarselService: InnboksVarselService = mockk(relaxed = true),
    beskjedVarselService: BeskjedVarselService = mockk(relaxed = true)

): Application.() -> Unit {
    return fun Application.() {
        authConfig()
        api(
            authConfig = {},
            oppgaveEventService = oppgaveEventService,
            innboksVarselService = innboksVarselService,
            beskjedVarselService = beskjedVarselService
        )
    }
}

fun mockAuthBuilder(): Application.() -> Unit = {
    installMockedAuthenticators {
        installTokenXAuthMock {
            setAsDefault = true

            alwaysAuthenticated = true
            staticUserPid = "123"
            staticSecurityLevel = SecurityLevel.LEVEL_4
        }
        installAzureAuthMock { }
    }
}

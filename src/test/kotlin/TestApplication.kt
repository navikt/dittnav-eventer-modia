import io.ktor.application.Application
import io.mockk.mockk
import no.nav.personbruker.dittnav.eventer.modia.beskjed.BeskjedEventService
import no.nav.personbruker.dittnav.eventer.modia.config.api
import no.nav.personbruker.dittnav.eventer.modia.innboks.InnboksEventService
import no.nav.personbruker.dittnav.eventer.modia.oppgave.OppgaveEventService
import no.nav.tms.token.support.authentication.installer.mock.installMockedAuthenticators
import no.nav.tms.token.support.tokenx.validation.mock.SecurityLevel

fun mockApi(
    authConfig: Application.() -> Unit = mockAuthBuilder(),
    oppgaveEventService: OppgaveEventService = mockk(relaxed = true),
    innboksEventService: InnboksEventService = mockk(relaxed = true),
    beskjedEventService: BeskjedEventService = mockk(relaxed = true)

): Application.() -> Unit {
    return fun Application.() {
        authConfig()
        api(
            authConfig = {},
            oppgaveEventService = oppgaveEventService,
            innboksEventService = innboksEventService,
            beskjedEventService = beskjedEventService
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

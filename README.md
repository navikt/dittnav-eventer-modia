# DittNAV event handler

Microservice som brukes for å lese inn eventer fra DittNAV sin event-cache (database). Den skriver også done-event-er til DittNAV sin kafka-topic.

# Kom i gang
1. Bygg dittnav-event-handler ved å kjøre `gradle build`
2. Start alle appens avhengigheter ved å kjøre `docker-compose -d`
3. Start appen ved å kjøre filen EventHandlerApplication eller kjør `./gradlew runServer`.
4. Appen nås på ´http://localhost:8092´

# Feilsøking
For å være sikker på at man får en ny tom database og tomme kafka-topics kan man kjøre kommandoen: `docker-compose down -v`

# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot https://github.com/orgs/navikt/teams/personbruker

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-personbruker.

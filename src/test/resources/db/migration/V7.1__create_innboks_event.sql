insert into innboks(produsent, eventTidspunkt, aktorId, eventId, dokumentId, tekst, link, sikkerhetsnivaa, sistOppdatert, aktiv)
values
    ('DittNAV', NOW(), '12345', '123', '100123', 'Dette er en dialognotifikasjon til brukeren', 'https://nav.no/systemX', 4, NOW(), true),
    ('DittNAV', NOW(), '12345', '345', '100456', 'Dette er en dialognotifikasjon til brukeren', 'https://nav.no/systemX', 4, NOW(), true),
    ('DittNAV', NOW(), '67890', '567', '100789', 'Dette er en dialognotifikasjon til brukeren', 'https://nav.no/systemX', 4, NOW(), true),
    ('DittNAV', NOW(), '67890', '789', '100789', 'Dette er en dialognotifikasjon til brukeren', 'https://nav.no/systemX', 4, NOW(), false);
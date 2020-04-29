create table if not exists beskjed
(
    id              serial not null
        constraint beskjed_pkey
            primary key,
    systembruker       varchar(100),
    eventtidspunkt  timestamp,
    fodselsnummer   varchar(50),
    eventid         varchar(50),
    grupperingsid   varchar(100),
    tekst           varchar(500),
    link            varchar(200),
    sikkerhetsnivaa integer,
    sistoppdatert   timestamp,
    aktiv           boolean,
    synligfremtil   timestamp,
    uid             varchar(100),
    constraint beskjedeventidprodusent
        unique (eventid, systembruker)
);

create table if not exists oppgave
(
    id              serial not null
        constraint oppgave_pkey
            primary key,
    systembruker       varchar(100),
    eventtidspunkt  timestamp,
    fodselsnummer   varchar(50),
    eventid         varchar(50),
    grupperingsid   varchar(100),
    tekst           varchar(500),
    link            varchar(200),
    sikkerhetsnivaa integer,
    sistoppdatert   timestamp,
    aktiv           boolean,
    constraint oppgaveeventidprodusent
        unique (eventid, systembruker)
);

create table if not exists innboks
(
    id              serial not null
        constraint innboks_pkey
            primary key,
    systembruker       varchar(100),
    eventtidspunkt  timestamp,
    fodselsnummer   varchar(50),
    eventid         varchar(50),
    grupperingsid   varchar(100),
    tekst           varchar(500),
    link            varchar(200),
    sikkerhetsnivaa integer,
    sistoppdatert   timestamp,
    aktiv           boolean,
    constraint innbokseventidprodusent
        unique (eventid, systembruker)
);

create table if not exists systembrukere (
    systembruker character varying(50) not null primary key,
    produsentnavn character varying(100) not null
);

create view if not exists brukernotifikasjon_view as
SELECT beskjed.eventid, beskjed.systembruker, 'beskjed' :: text AS type, beskjed.fodselsnummer, beskjed.aktiv
FROM beskjed
UNION
SELECT oppgave.eventid, oppgave.systembruker, 'oppgave' :: text AS type, oppgave.fodselsnummer, oppgave.aktiv
FROM oppgave
UNION
SELECT innboks.eventid, innboks.systembruker, 'innboks' :: text AS type, innboks.fodselsnummer, innboks.aktiv
FROM innboks;


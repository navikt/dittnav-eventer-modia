package no.nav.personbruker.dittnav.eventhandler.done

import Beskjed
import no.nav.brukernotifikasjon.schemas.Done
import no.nav.brukernotifikasjon.schemas.Nokkel
import no.nav.personbruker.dittnav.eventhandler.config.Environment
import no.nav.personbruker.dittnav.eventhandler.config.Kafka
import no.nav.personbruker.dittnav.eventhandler.config.Kafka.doneTopicName
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.errors.TimeoutException
import org.slf4j.LoggerFactory

class DoneProducer(private val env: Environment) {

    private val log = LoggerFactory.getLogger(DoneProducer::class.java)

    private val kafkaProducer = KafkaProducer<Nokkel, Done>(Kafka.producerProps(env))

    fun produceDoneEventForSuppliedEventId(fodselsnummer: String, eventId: String, beskjed: Beskjed) {
        val doneKey = createKeyForEvent(eventId, beskjed.produsent)
        val doneEvent = createDoneEvent(fodselsnummer, beskjed.grupperingsId)
        kafkaProducer.send(ProducerRecord(doneTopicName, doneKey, doneEvent), Callback { metadata, exception ->
            if(exception != null) {
                when(exception) {
                    TimeoutException::class.java -> log.warn("Fikk timeout ved produsering av Done-event for Beskjed-event med eventId $eventId.", exception)
                    else -> log.error("Klarte ikke produsere Done-event for Beskjed-event med eventId $eventId.", exception)
                }
            }
        })
    }

    fun close() {
        try {
            kafkaProducer.close()
            log.info("Produsent for Done-eventer er lukket.")
        } catch(e: Exception) {
            log.warn("Klarte ikke å lukke produsent for Done-eventer. Det kan være eventer som ikke ble produsert.")
        }
    }
}

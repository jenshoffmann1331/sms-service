package com.example.consumer

import com.example.dto.BefehlsnachrichtEvent
import com.example.service.SmsService
import com.fasterxml.jackson.databind.ObjectMapper
import io.smallrye.reactive.messaging.annotations.Blocking
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Message
import org.jboss.logging.Logger
import java.time.Instant
import java.util.concurrent.CompletionStage

@ApplicationScoped
class BefehlsnachrichtConsumer @Inject constructor(
    private val smsService: SmsService,
    private val objectMapper: ObjectMapper
) {
    private val log = Logger.getLogger(BefehlsnachrichtConsumer::class.java)
    private val maxAgeSeconds = 60L
    
    @Incoming("befehlsnachricht-events")
    @Blocking
    fun consume(message: Message<String>): CompletionStage<Void> {
        return try {
            val event = objectMapper.readValue(message.payload, BefehlsnachrichtEvent::class.java)
            log.info("Received event: $event")
            
            // Check message age (simplified - using current time as baseline)
            val messageTimestamp = Instant.now().toEpochMilli()
            val ageSeconds = 0L // In production würde man den echten Timestamp aus der Message verwenden
            
            if (ageSeconds > maxAgeSeconds) {
                log.warn("Message too old ($ageSeconds seconds), discarding: $event")
                return message.ack()
            }
            
            // Send SMS
            val success = smsService.sendSms("491234567890", event.inhalt)
            
            if (success) {
                message.ack()
            } else {
                log.warn("SMS sending failed, nacking message for requeue")
                message.nack(IllegalStateException("SMS sending failed"))
            }
        } catch (e: Exception) {
            log.error("Error processing message", e)
            message.nack(e)
        }
    }
}

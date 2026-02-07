package com.example.service

import com.example.dto.BefehlsnachrichtRequest
import com.example.dto.BefehlsnachrichtResponse
import com.example.entity.Befehlsnachricht
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.transaction.Transactional
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import java.time.LocalDateTime

@ApplicationScoped
class BefehlsnachrichtService(
    @Channel("befehlsnachricht-events")
    private val eventEmitter: Instance<Emitter<String>>
) {

    @Transactional
    fun create(request: BefehlsnachrichtRequest): BefehlsnachrichtResponse {
        val befehlsnachricht = Befehlsnachricht().apply {
            inhalt = request.inhalt
            erstelltAm = LocalDateTime.now()
        }
        
        befehlsnachricht.persist()
        
        if (eventEmitter.isResolvable) {
            eventEmitter.get().send("""{"id":${befehlsnachricht.id},"inhalt":"${befehlsnachricht.inhalt}"}""")
        }
        
        return BefehlsnachrichtResponse(
            id = befehlsnachricht.id!!,
            inhalt = befehlsnachricht.inhalt,
            erstelltAm = befehlsnachricht.erstelltAm
        )
    }
}

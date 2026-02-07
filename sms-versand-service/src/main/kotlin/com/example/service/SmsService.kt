package com.example.service

import com.example.smpp.SmppSessionManager
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.jboss.logging.Logger
import org.jsmpp.bean.ESMClass
import org.jsmpp.bean.GeneralDataCoding
import org.jsmpp.bean.NumberingPlanIndicator
import org.jsmpp.bean.RegisteredDelivery
import org.jsmpp.bean.TypeOfNumber

@ApplicationScoped
class SmsService @Inject constructor(
    private val sessionManager: SmppSessionManager
) {
    private val log = Logger.getLogger(SmsService::class.java)
    
    fun sendSms(destination: String, message: String): Boolean {
        val session = sessionManager.getNextSession()
        
        if (session == null) {
            log.error("No SMPP session available for sending SMS")
            return false
        }
        
        return try {
            val messageId = session.submitShortMessage(
                "CMT",
                TypeOfNumber.INTERNATIONAL,
                NumberingPlanIndicator.UNKNOWN,
                "1234",
                TypeOfNumber.INTERNATIONAL,
                NumberingPlanIndicator.UNKNOWN,
                destination,
                ESMClass(),
                0.toByte(),
                1.toByte(),
                null,
                null,
                RegisteredDelivery(),
                0.toByte(),
                GeneralDataCoding(),
                0.toByte(),
                message.toByteArray()
            )
            
            log.info("SMS sent successfully. MessageId: $messageId, Destination: $destination, Message: $message")
            true
        } catch (e: Exception) {
            log.error("Failed to send SMS to $destination", e)
            false
        }
    }
}

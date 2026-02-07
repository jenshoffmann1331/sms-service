package com.example.smpp

import com.example.config.SmscConfig
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.jboss.logging.Logger
import org.jsmpp.bean.BindType
import org.jsmpp.bean.NumberingPlanIndicator
import org.jsmpp.bean.TypeOfNumber
import org.jsmpp.session.BindParameter
import org.jsmpp.session.SMPPSession
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@ApplicationScoped
@Startup
class SmppSessionManager @Inject constructor(
    private val smscConfig: SmscConfig
) {
    private val log = Logger.getLogger(SmppSessionManager::class.java)
    private val sessions = ConcurrentHashMap<String, SMPPSession>()
    private val roundRobinIndex = AtomicInteger(0)
    
    init {
        initializeSessions()
    }
    
    private fun initializeSessions() {
        smscConfig.instances().forEachIndexed { index, instance ->
            val sessionId = "smsc-$index"
            Thread {
                try {
                    val session = createAndBindSession(instance, sessionId)
                    sessions[sessionId] = session
                    log.info("SMPP session $sessionId bound successfully to ${instance.host()}:${instance.port()}")
                } catch (e: Exception) {
                    log.error("Failed to bind SMPP session $sessionId", e)
                }
            }.start()
        }
    }
    
    private fun createAndBindSession(instance: SmscConfig.SmscInstance, sessionId: String): SMPPSession {
        val session = SMPPSession()
        
        val bindParam = BindParameter(
            BindType.BIND_TX,
            instance.systemId(),
            instance.password(),
            "cp",
            TypeOfNumber.UNKNOWN,
            NumberingPlanIndicator.UNKNOWN,
            null
        )
        
        session.connectAndBind(instance.host(), instance.port(), bindParam)
        return session
    }
    
    fun getNextSession(): SMPPSession? {
        if (sessions.isEmpty()) {
            log.warn("No SMPP sessions available")
            return null
        }
        
        val sessionList = sessions.values.toList()
        if (sessionList.isEmpty()) {
            log.warn("No SMPP sessions available")
            return null
        }
        
        val index = roundRobinIndex.getAndIncrement() % sessionList.size
        return sessionList[index]
    }
    
    fun closeAllSessions() {
        sessions.values.forEach { session ->
            try {
                session.unbindAndClose()
            } catch (e: Exception) {
                log.error("Error closing session", e)
            }
        }
        sessions.clear()
    }
}

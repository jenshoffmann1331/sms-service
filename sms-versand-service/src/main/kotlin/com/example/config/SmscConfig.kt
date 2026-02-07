package com.example.config

import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "smsc")
interface SmscConfig {
    fun instances(): List<SmscInstance>
    
    interface SmscInstance {
        fun host(): String
        fun port(): Int
        fun systemId(): String
        fun password(): String
    }
}

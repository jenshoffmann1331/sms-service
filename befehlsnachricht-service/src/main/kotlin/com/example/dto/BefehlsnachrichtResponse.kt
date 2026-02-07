package com.example.dto

import java.time.LocalDateTime

data class BefehlsnachrichtResponse(
    val id: Long,
    val inhalt: String,
    val erstelltAm: LocalDateTime
)

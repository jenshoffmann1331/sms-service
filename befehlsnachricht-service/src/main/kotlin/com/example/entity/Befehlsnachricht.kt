package com.example.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
class Befehlsnachricht : PanacheEntity() {
    lateinit var inhalt: String
    lateinit var erstelltAm: LocalDateTime
}

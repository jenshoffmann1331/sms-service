package com.example.dto

class BefehlsnachrichtRequest() {
    var inhalt: String = ""
    
    constructor(inhalt: String) : this() {
        this.inhalt = inhalt
    }
}

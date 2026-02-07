package com.example.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import jakarta.ws.rs.core.Response

@QuarkusTest
class BefehlsnachrichtResourceTest {

    @Test
    fun `should create befehlsnachricht`() {
        given()
            .contentType(ContentType.JSON)
            .body("""{"inhalt":"Test Nachricht"}""")
            .`when`()
            .post("/befehlsnachricht")
            .then()
            .statusCode(Response.Status.CREATED.statusCode)
            .body("id", notNullValue())
            .body("inhalt", org.hamcrest.CoreMatchers.`is`("Test Nachricht"))
            .body("erstelltAm", notNullValue())
    }
}

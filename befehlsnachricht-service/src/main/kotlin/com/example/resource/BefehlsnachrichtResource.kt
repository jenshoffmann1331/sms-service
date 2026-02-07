package com.example.resource

import com.example.dto.BefehlsnachrichtRequest
import com.example.dto.BefehlsnachrichtResponse
import com.example.service.BefehlsnachrichtService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/befehlsnachricht")
class BefehlsnachrichtResource(
    private val service: BefehlsnachrichtService
) {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(request: BefehlsnachrichtRequest): Response {
        val response = service.create(request)
        return Response.status(Response.Status.CREATED).entity(response).build()
    }
}

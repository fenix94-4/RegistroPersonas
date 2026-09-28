package com.example.registropersonas

import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PersonasApi {
    @GET("person")
    suspend fun obtenerPerosnas(): List<Persona>

    @POST("person")
    suspend fun registrarPersona(@Body persona: Persona): Persona

    @PUT("person/{id}")
    suspend fun actualizarPersona(
        @Path("id") id: String,
        @Body persona: Persona
    ): Persona

    @DELETE("person/{id}")
    suspend fun eliminarPersona(
        @Path("id") id: String
    ): Persona
}
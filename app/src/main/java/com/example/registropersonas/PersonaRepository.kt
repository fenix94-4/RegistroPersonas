package com.example.registropersonas

class PersonaRepository {

    private val api = RetrofitInstance.api

    suspend fun obtenerPersonas(): List<Persona> {
        return api.obtenerPerosnas()
    }

    suspend fun registrarPersona(persona: Persona): Persona {
        return api.registrarPersona(persona)
    }

    suspend fun actualizarPersona(
        id: String,
        persona: Persona
    ): Persona {
        return api.actualizarPersona(id, persona)
    }

    suspend fun eliminarPersona(id: String): Persona {
        return api.eliminarPersona(id)
    }


}
package com.example.registropersonas

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch


class PersonaViewModel : ViewModel() {
    var personas by mutableStateOf<List<Persona>>(emptyList())
        private set
    var cargando by mutableStateOf(false)
        private set

    var guardando by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    private val repository = PersonaRepository()

    fun cargarPersonas() {
        viewModelScope.launch {
            cargando = true
            error = null


            try {
                personas = repository.obtenerPersonas()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                error = "No se pudieron cargar las personas"
            } finally {
                cargando = false
            }
        }
    }


    fun registrarPersona(
        persona: Persona,
        alGuardar: () -> Unit
    ) {
        if (guardando) return

        if (
            persona.nombre.isBlank() ||
            persona.pais.isBlank() ||
            persona.direccion.isBlank() ||
            persona.trabajo.isBlank()
        ) {
            error = "Completa nombre, país, dirección y trabajo"
            return
        }

        viewModelScope.launch {
            guardando = true
            error = null

            try {
                val personaRegistrada =
                    repository.registrarPersona(persona)

                personas = personas + personaRegistrada
                alGuardar()

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                error = "No se pudo registrar la persona"
            } finally {
                guardando = false
            }
        }
    }

    fun actualizarPersona(
        id: String,
        persona: Persona,
        alActualizar: () -> Unit
    ) {
        if (guardando || cargando) return

        if (
            persona.nombre.isBlank() ||
            persona.pais.isBlank() ||
            persona.direccion.isBlank() ||
            persona.trabajo.isBlank()
        ) {
            error = "Completa nombre, país, dirección y trabajo"
            return
        }

        viewModelScope.launch {
            guardando = true
            error = null

            try {
                val personaActualizada =
                    repository.actualizarPersona(id, persona)

                personas = personas.map { actual ->
                    if (actual.id == id) personaActualizada else actual
                }

                alActualizar()

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                error = "No se pudo actualizar la persona"
            } finally {
                guardando = false
            }
        }
    }

    fun eliminarPersona(
        id: String,
        alEliminar: () -> Unit
    ) {
        if (guardando || cargando) return

        viewModelScope.launch {
            guardando = true
            error = null

            try {
                repository.eliminarPersona(id)

                personas = personas.filter { persona ->
                    persona.id != id
                }

                alEliminar()

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                error = "No se pudo eliminar la persona"
            } finally {
                guardando = false
            }
        }
    }

}

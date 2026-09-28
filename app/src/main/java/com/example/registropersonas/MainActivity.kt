package com.example.registropersonas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.registropersonas.ui.theme.RegistroPersonasTheme
import androidx.compose.material3.AlertDialog


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val personaViewModel =
            ViewModelProvider(this)[PersonaViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            RegistroPersonasTheme {

                var nombre by rememberSaveable { mutableStateOf("") }
                var direccion by rememberSaveable { mutableStateOf("") }
                var trabajo by rememberSaveable { mutableStateOf("") }
                var estadocivil by rememberSaveable { mutableStateOf(false) }
                var pais by rememberSaveable { mutableStateOf("") }
                var menuPaisAbierto by rememberSaveable {
                    mutableStateOf(false)
                }
                var idEnEdicion by rememberSaveable {
                    mutableStateOf<String?>(null)
                }

                                                                                                                                                                                    var idPorEliminar by rememberSaveable {
                    mutableStateOf<String?>(null)
                }
                var nombrePorEliminar by rememberSaveable {
                    mutableStateOf("")
                }

                val paises = listOf(
                    "Bolivia", "Argentina", "Brasil", "Chile", "Perú"
                )

                val ocupado =
                    personaViewModel.guardando || personaViewModel.cargando

                val limpiarFormulario: () -> Unit = {
                    nombre = ""
                    direccion = ""
                    trabajo = ""
                    estadocivil = false
                    pais = ""
                    menuPaisAbierto = false
                    idEnEdicion = null
                }

                if (idPorEliminar != null) {
                    AlertDialog(
                        onDismissRequest = {
                            idPorEliminar = null
                        },
                        title = {
                            Text("Confirmar eliminación")
                        },
                        text = {
                            Text(
                                "¿Seguro que deseas eliminar a $nombrePorEliminar? " +
                                        "Esta acción no se puede deshacer."
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    val id = idPorEliminar

                                    if (id != null) {
                                        personaViewModel.eliminarPersona(
                                            id = id,
                                            alEliminar = {
                                                if (idEnEdicion == id) {
                                                    limpiarFormulario()
                                                }
                                            }
                                        )
                                    }

                                    idPorEliminar = null
                                },
                                enabled = !ocupado
                            ) {
                                Text("Eliminar")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    idPorEliminar = null
                                }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {

                        TextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            enabled = !ocupado
                        )

                        Box {
                            Button(
                                onClick = { menuPaisAbierto = true },
                                enabled = !ocupado
                            ) {
                                Text(
                                    if (pais.isEmpty()) "Seleccionar país"
                                    else pais
                                )
                            }

                            DropdownMenu(
                                expanded = menuPaisAbierto,
                                onDismissRequest = {
                                    menuPaisAbierto = false
                                }
                            ) {
                                paises.forEach { opcion ->
                                    DropdownMenuItem(
                                        text = { Text(opcion) },
                                        onClick = {
                                            pais = opcion
                                            menuPaisAbierto = false
                                        }
                                    )
                                }
                            }
                        }

                        TextField(
                            value = direccion,
                            onValueChange = { direccion = it },
                            label = { Text("Dirección") },
                            enabled = !ocupado
                        )

                        TextField(
                            value = trabajo,
                            onValueChange = { trabajo = it },
                            label = { Text("Trabajo") },
                            enabled = !ocupado
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("¿Está casado?")

                            Switch(
                                checked = estadocivil,
                                onCheckedChange = { estadocivil = it },
                                enabled = !ocupado
                            )
                        }

                        Button(
                            onClick = {
                                val datosPersona = Persona(
                                    nombre = nombre.trim(),
                                    pais = pais,
                                    direccion = direccion.trim(),
                                    estadocivil = estadocivil,
                                    trabajo = trabajo.trim()
                                )

                                val idActual = idEnEdicion

                                if (idActual == null) {
                                    personaViewModel.registrarPersona(
                                        persona = datosPersona,
                                        alGuardar = limpiarFormulario
                                    )
                                } else {
                                    personaViewModel.actualizarPersona(
                                        id = idActual,
                                        persona = datosPersona,
                                        alActualizar = limpiarFormulario
                                    )
                                }
                            },
                            enabled = !ocupado
                        ) {
                            Text(
                                when {
                                    personaViewModel.guardando ->
                                        "Guardando..."

                                    idEnEdicion != null ->
                                        "Guardar cambios"

                                    else -> "Registrar"
                                }
                            )
                        }

                        if (idEnEdicion != null) {
                            Button(
                                onClick = limpiarFormulario,
                                enabled = !ocupado
                            ) {
                                Text("Cancelar edición")
                            }
                        }

                        Button(
                            onClick = {
                                personaViewModel.cargarPersonas()
                            },
                            enabled = !ocupado
                        ) {
                            Text("Cargar personas")
                        }

                        if (personaViewModel.cargando) {
                            Text("Cargando...")
                        }

                        val mensajeError = personaViewModel.error

                        if (mensajeError != null) {
                            Text(mensajeError)
                        }

                        Text(
                            "Personas cargadas: ${personaViewModel.personas.size}"
                        )

                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(personaViewModel.personas) { persona ->
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text("Nombre: ${persona.nombre}")
                                    Text("País: ${persona.pais}")
                                    Text("Dirección: ${persona.direccion}")
                                    Text(
                                        "Estado civil (casado): ${
                                            if (persona.estadocivil) "Sí"
                                            else "No"
                                        }"
                                    )
                                    Text("Trabajo: ${persona.trabajo}")

                                    Button(
                                        onClick = {
                                            idEnEdicion = persona.id
                                            nombre = persona.nombre
                                            pais = persona.pais
                                            direccion = persona.direccion
                                            estadocivil = persona.estadocivil
                                            trabajo = persona.trabajo
                                        },
                                        enabled = persona.id != null &&
                                                !ocupado
                                    ) {
                                        Text("Editar")
                                    }

                                    Button(
                                        onClick = {
                                            idPorEliminar = persona.id
                                            nombrePorEliminar = persona.nombre
                                        },
                                        enabled = persona.id != null && !ocupado
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
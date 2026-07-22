package com.example.taskify.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.taskify.components.BottomMenu
import com.example.taskify.components.NuevaAlarmaDialog
import com.example.taskify.components.PantallaSeleccionada
import com.example.taskify.components.TarjetaAlarma
import com.example.taskify.viewmodel.AlarmaViewModel

/**
 * Pantalla de alarmas. Muestra la lista de alarmas (manuales y las generadas
 * automáticamente por tareas con recordatorio) en un LazyColumn scrolleable.
 * Permite agregar alarmas manuales con el botón + y navegar con el menú inferior.
 */
@Composable
fun PantallaAlarmas(
    navController: NavHostController,
    alarmaViewModel: AlarmaViewModel = viewModel() // viewModel() reutiliza instancia existente
) {
    // Refresca la lista al entrar, para mostrar alarmas creadas desde PantallaTareas
    LaunchedEffect(Unit) {
        alarmaViewModel.refrescarAlarmas()
    }

    var mostrarDialogo by remember { mutableStateOf(false) } // Controla visibilidad del diálogo

    val listaAlarmas = alarmaViewModel.listaAlarmas // Lista observable de alarmas

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFE4F2F7))
    ) {

        // 1. Contenido principal: título + lista de alarmas
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text(
                text = "Alarmas",
                color = Color(0xFF12171A),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista scrolleable con padding inferior para no tapar tarjetas con el menú
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 200.dp)
            ) {
                items(items = listaAlarmas, key = { it.id }) { alarma ->
                    TarjetaAlarma(
                        alarma = alarma,
                        onEliminar = { alarmaViewModel.eliminarAlarma(alarma) },
                        onActivaChange = { estado -> alarmaViewModel.cambiarEstadoAlarma(alarma, estado) },
                        onDiasChange = { nuevosDias -> alarmaViewModel.cambiarDiasAlarma(alarma, nuevosDias) }
                    )
                }
            }
        }

        // 2. Botón flotante circular para abrir el diálogo de nueva alarma
        FloatingActionButton(
            onClick = { mostrarDialogo = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 45.dp, bottom = 150.dp)
                .size(72.dp),
            containerColor = Color(0xFF68C3E5),
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar alarma", tint = Color.White, modifier = Modifier.size(36.dp))
        }

        // 3. Menú inferior de navegación
        BottomMenu(
            pantallaActual = PantallaSeleccionada.ALARMAS,
            onPantallaSeleccionada = { pantalla ->
                when (pantalla) {
                    PantallaSeleccionada.TAREAS -> navController.navigate("tareas")
                    PantallaSeleccionada.ALARMAS -> {}  // Ya estamos aquí, no navegar
                    PantallaSeleccionada.CALENDARIO -> navController.navigate("calendario")
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // 4. Diálogo de nueva alarma (solo visible cuando mostrarDialogo = true)
        if (mostrarDialogo) {
            NuevaAlarmaDialog(
                onDismiss = { mostrarDialogo = false },
                onGuardar = { alarma ->
                    alarmaViewModel.agregarAlarma(alarma)
                    mostrarDialogo = false
                }
            )
        }
    }
}
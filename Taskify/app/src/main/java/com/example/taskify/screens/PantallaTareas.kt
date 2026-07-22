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
import androidx.navigation.NavController
import com.example.taskify.components.BottomMenu
import com.example.taskify.components.NuevaTareaDialog
import com.example.taskify.components.PantallaSeleccionada
import com.example.taskify.components.TarjetaTarea
import com.example.taskify.viewmodel.TaskViewModel

/**
 * Pantalla principal de tareas. Muestra la lista de tareas en un LazyColumn
 * y permite agregar nuevas con el botón flotante (+). Incluye el menú inferior
 * para navegar entre pantallas. El ViewModel se comparte via viewModel() para
 * que Compose reutilice la misma instancia mientras la pantalla está en el back stack.
 */
@Composable
fun PantallaTareas(
    navController: NavController,
    tareasViewModel: TaskViewModel = viewModel() // viewModel() reutiliza instancia existente
) {
    // Refresca la lista al entrar, por si AlarmaViewModel modificó alguna tarea
    LaunchedEffect(Unit) {
        tareasViewModel.refrescarTareas()
    }

    var mostrarDialogo by remember { mutableStateOf(false) } // Controla visibilidad del diálogo

    val listaTareas = tareasViewModel.listaTareas // Lista observable de tareas

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFE4F2F7))) {

        // 1. Contenido principal: título + lista de tareas
        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text(
                text = "Tareas",
                color = Color(0xFF12171A),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Lista scrolleable con padding inferior para no tapar las tarjetas con el menú
            LazyColumn(contentPadding = PaddingValues(bottom = 120.dp)) {
                items(items = listaTareas, key = { it.id }) { tarea ->
                    TarjetaTarea(
                        tarea = tarea,
                        onCheckedChange = { estado ->
                            if (estado) tareasViewModel.eliminarTarea(tarea) // Marcar = eliminar
                        },
                        onReminderClick = {
                            tareasViewModel.cambiarRecordatorioTarea(tarea, !tarea.recordatorio)
                        }
                    )
                }
            }
        }

        // 2. Botón flotante circular para abrir el diálogo de nueva tarea
        FloatingActionButton(
            onClick = { mostrarDialogo = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 45.dp, bottom = 150.dp)
                .size(72.dp),
            containerColor = Color(0xFF68C3E5),
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar tarea", tint = Color.White, modifier = Modifier.size(36.dp))
        }

        // 3. Menú inferior de navegación
        BottomMenu(
            pantallaActual = PantallaSeleccionada.TAREAS,
            onPantallaSeleccionada = { pantalla ->
                when (pantalla) {
                    PantallaSeleccionada.TAREAS -> {}  // Ya estamos aquí, no navegar
                    PantallaSeleccionada.ALARMAS -> navController.navigate("alarmas")
                    PantallaSeleccionada.CALENDARIO -> navController.navigate("calendario")
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // 4. Diálogo de nueva tarea (solo visible cuando mostrarDialogo = true)
        if (mostrarDialogo) {
            NuevaTareaDialog(
                onDismiss = { mostrarDialogo = false },
                onGuardar = { tarea ->
                    tareasViewModel.agregarTarea(tarea)
                    mostrarDialogo = false
                }
            )
        }
    }
}
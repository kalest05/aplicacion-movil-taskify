package com.example.taskify.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskify.ui.theme.AzulOscuro

// Enum que identifica cuál de las tres pantallas principales está activa
enum class PantallaSeleccionada {
    TAREAS,
    ALARMAS,
    CALENDARIO
}

/**
 * Barra de navegación inferior compartida por las tres pantallas.
 * Recibe cuál pantalla está activa para resaltar el ícono correcto,
 * y notifica al llamador con un callback cuando el usuario toca otro ícono.
 * Cada pantalla decide qué hacer al recibir esa notificación.
 */
@Composable
fun BottomMenu(
    pantallaActual: PantallaSeleccionada,
    onPantallaSeleccionada: (PantallaSeleccionada) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(
                RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp) // Esquinas redondeadas arriba
            ),
        containerColor = Color.White
    ) {
        // Ítem: Tareas
        NavigationBarItem(
            selected = pantallaActual == PantallaSeleccionada.TAREAS,
            onClick = { onPantallaSeleccionada(PantallaSeleccionada.TAREAS) },
            icon = {
                Icon(imageVector = Icons.Default.Checklist, contentDescription = "Tareas")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AzulOscuro,
                unselectedIconColor = Color.Gray,
                indicatorColor = Color.Transparent // Sin fondo al seleccionar
            )
        )

        // Ítem: Alarmas
        NavigationBarItem(
            selected = pantallaActual == PantallaSeleccionada.ALARMAS,
            onClick = { onPantallaSeleccionada(PantallaSeleccionada.ALARMAS) },
            icon = {
                Icon(imageVector = Icons.Default.Alarm, contentDescription = "Alarmas")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AzulOscuro,
                unselectedIconColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        // Ítem: Calendario
        NavigationBarItem(
            selected = pantallaActual == PantallaSeleccionada.CALENDARIO,
            onClick = { onPantallaSeleccionada(PantallaSeleccionada.CALENDARIO) },
            icon = {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendario")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AzulOscuro,
                unselectedIconColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
package com.example.taskify.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskify.model.Prioridad
import com.example.taskify.model.Tareas
import com.example.taskify.ui.theme.AzulOscuro
import com.example.taskify.ui.theme.FondoModal
import com.example.taskify.ui.theme.TextoPrincipal

/**
 * Devuelve el color correspondiente al nivel de prioridad de una tarea.
 * Se usa para pintar la barra lateral de color en cada tarjeta.
 * prioridad es el nivel de urgencia de la tarea
 */
fun obtenerColorPrioridad(prioridad: Prioridad): Color {
    return when (prioridad) {
        Prioridad.ALTA -> Color(0xFFF44336)   // Rojo
        Prioridad.MEDIA -> Color(0xFFFFC107)  // Amarillo
        Prioridad.BAJA -> Color(0xFF4CAF50)   // Verde
    }
}

/**
 * Tarjeta visual que representa una tarea en la lista.
 * Muestra una barra de color según prioridad, checkbox para completar,
 * el título, descripción, fecha/hora, y un botón de recordatorio.
 * Notifica al ViewModel mediante callbacks cuando el usuario interactúa.
 */
@Composable
fun TarjetaTarea(
    tarea: Tareas,
    onCheckedChange: (Boolean) -> Unit,
    onReminderClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FondoModal)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            // Barra vertical de color según prioridad
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(70.dp)
                    .background(obtenerColorPrioridad(tarea.prioridad))
            )

            // Checkbox: al marcarse, notifica para eliminar la tarea
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { nuevoEstado -> onCheckedChange(nuevoEstado) },
                colors = CheckboxDefaults.colors(
                    checkedColor = AzulOscuro,
                    uncheckedColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Columna con texto de la tarea
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tarea.titulo, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                Text(text = tarea.descripcion, color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                // Muestra fecha y hora juntas separadas por un punto
                Text(text = "${tarea.fecha} • ${tarea.hora}", color = Color.Gray, fontSize = 11.sp)
            }

            // Ícono de recordatorio: cambia entre campana activa e inactiva
            IconButton(onClick = onReminderClick) {
                Icon(
                    imageVector = if (tarea.recordatorio) Icons.Default.Notifications
                    else Icons.Default.NotificationsOff,
                    contentDescription = null,
                    tint = if (tarea.recordatorio) AzulOscuro else Color.Gray
                )
            }
        }
    }
}
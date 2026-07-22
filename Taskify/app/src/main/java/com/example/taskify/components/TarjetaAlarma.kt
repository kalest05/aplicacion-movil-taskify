package com.example.taskify.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskify.model.Alarma
import com.example.taskify.model.DiaSemana
import com.example.taskify.ui.theme.AzulOscuro
import com.example.taskify.ui.theme.FondoModal
import com.example.taskify.ui.theme.TextoPrincipal

/**
 * Tarjeta visual que representa una alarma en la lista.
 * Muestra la hora en grande, descripción, días de la semana como círculos
 * y un switch para activar/desactivar. Los días son tocables solo si la
 * alarma no está vinculada a una tarea.
 */
@Composable
fun TarjetaAlarma(
    alarma: Alarma,
    onEliminar: () -> Unit,
    onActivaChange: (Boolean) -> Unit,
    onDiasChange: (List<DiaSemana>) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = FondoModal)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)) {

            // Fila superior: hora grande + botón X + switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hora en tamaño grande (36sp)
                Text(
                    text = alarma.hora,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Light,
                    color = TextoPrincipal,
                    modifier = Modifier.weight(1f)
                )

                // Botón eliminar (X rojo)
                IconButton(onClick = onEliminar, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Eliminar alarma",
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Switch para encender/apagar la alarma
                Switch(
                    checked = alarma.activa,
                    onCheckedChange = onActivaChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = AzulOscuro,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCCCCCC)
                    )
                )
            }

            // Texto descriptivo de la alarma
            Text(
                text = alarma.descripcion,
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )

            // Fila de círculos de días de la semana
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                DiaSemana.entries.forEach { dia ->
                    val estaActivo = alarma.dias.contains(dia)
                    // Las alarmas vinculadas a tareas no permiten cambiar días
                    val esModificable = alarma.tareaId == null

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(color = if (estaActivo) AzulOscuro else Color(0xFFCCCCCC))
                            .then(
                                // Solo agrega el clickable si la alarma es manual
                                if (esModificable) {
                                    Modifier.clickable {
                                        val nuevosDias = if (estaActivo) {
                                            alarma.dias.filter { it != dia } // Quitar día
                                        } else {
                                            alarma.dias + dia               // Agregar día
                                        }
                                        onDiasChange(nuevosDias)
                                    }
                                } else Modifier
                            )
                    ) {
                        Text(
                            text = dia.etiqueta,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
package com.example.taskify.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskify.model.Alarma
import com.example.taskify.model.DiaSemana
import com.example.taskify.ui.theme.AzulBoton
import com.example.taskify.ui.theme.AzulOscuro
import com.example.taskify.ui.theme.FondoModal
import com.example.taskify.ui.theme.TextoPrincipal
import java.util.Calendar
import java.util.Locale

/**
 * Diálogo modal para crear una nueva alarma de forma manual.
 * Permite seleccionar hora (via TimePicker), descripción, días de la semana
 * y si debe activarse al crearse. Al confirmar entrega el objeto Alarma al ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaAlarmaDialog(
    onDismiss: () -> Unit,
    onGuardar: (Alarma) -> Unit
) {
    var descripcion by remember { mutableStateOf("") }                              // Texto descriptivo opcional
    var hora by remember { mutableStateOf("7:00") }                                 // Hora de la alarma en "HH:mm"
    var diasSeleccionados by remember { mutableStateOf(DiaSemana.entries.toList()) } // Días activos (todos por defecto)
    var activarAlarma by remember { mutableStateOf(true) }                          // Estado del switch al crear
    var mostrarTimePicker by remember { mutableStateOf(false) }                     // Controla el sub-diálogo de hora

    // Instancia de Calendar para inicializar el TimePicker con la hora actual
    val calendar = Calendar.getInstance()

    AlertDialog(
        containerColor = FondoModal,
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Nueva alarma", color = Color(0xFF565050), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                // Campo de hora (solo lectura, abre TimePicker al tocar el campo)
                Text(text = "Hora", color = Color(0xFF9A9898), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = hora,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    trailingIcon = { Text("▼", color = Color.Gray, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.LightGray,
                        disabledTextColor = TextoPrincipal,
                        disabledLabelColor = Color.Gray
                    ),
                    // El campo está deshabilitado pero el clickable activa el TimePicker
                    modifier = Modifier.fillMaxWidth().clickable { mostrarTimePicker = true },
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Campo de descripción opcional
                Text(text = "Descripción (opcional)", color = Color(0xFF9A9898), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = { Text("Añade detalles sobre una tarea...", color = Color.LightGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulOscuro, unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = AzulOscuro, unfocusedLabelColor = Color.Gray, cursorColor = AzulOscuro
                    ),
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(14.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de días: círculos azules = activos, grises = inactivos
                Text(text = "Repetir", color = Color(0xFF9A9898), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    DiaSemana.entries.forEach { dia ->
                        val seleccionado = diasSeleccionados.contains(dia)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(34.dp)
                                .background(
                                    color = if (seleccionado) AzulOscuro else Color(0xFFDDDDDD),
                                    shape = CircleShape
                                )
                                .clickable {
                                    // Agrega o quita el día de la lista según su estado actual
                                    diasSeleccionados = if (seleccionado)
                                        diasSeleccionados - dia
                                    else
                                        diasSeleccionados + dia
                                }
                        ) {
                            Text(text = dia.etiqueta, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Selecciona los días para repetir la alarma", color = Color(0xFF9A9898), fontSize = 11.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Switch para decidir si la alarma empieza activa o inactiva
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(text = "Activar alarma", color = Color(0xFF9A9898))
                        Text(text = "La alarma se activará al crearla", color = Color(0xFFBBBBBB), fontSize = 11.sp)
                    }
                    Switch(
                        checked = activarAlarma,
                        onCheckedChange = { activarAlarma = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AzulOscuro, checkedTrackColor = AzulBoton, checkedBorderColor = AzulOscuro
                        )
                    )
                }
            }
        },
        confirmButton = {
            // Construye el objeto Alarma sin tareaId (es manual, no vinculada a tarea)
            Button(
                onClick = {
                    onGuardar(Alarma(hora = hora, descripcion = descripcion, dias = diasSeleccionados, activa = activarAlarma))
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulBoton)
            ) { Text("Crear Alarma", color = Color.Black) }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }, shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
            ) { Text("Cancelar", color = Color.Black) }
        }
    )

    // Sub-diálogo: TimePicker para seleccionar la hora de la alarma
    if (mostrarTimePicker) {
        val estadoHora = rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE),
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { mostrarTimePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        // Formatea con ceros a la izquierda para mantener formato "HH:mm"
                        hora = String.format(Locale.getDefault(), "%02d:%02d", estadoHora.hour, estadoHora.minute)
                        mostrarTimePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulOscuro, contentColor = Color.White)
                ) { Text("Aceptar") }
            },
            dismissButton = {
                Button(onClick = { mostrarTimePicker = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0), contentColor = Color.Black)
                ) { Text("Cancelar") }
            },
            text = {
                TimePicker(
                    state = estadoHora,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color(0xFFEAEAEA), clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = TextoPrincipal, selectorColor = AzulOscuro,
                        timeSelectorSelectedContainerColor = AzulOscuro,
                        timeSelectorUnselectedContainerColor = Color(0xFFEAEAEA),
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = TextoPrincipal
                    )
                )
            }
        )
    }
}
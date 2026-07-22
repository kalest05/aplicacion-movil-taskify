package com.example.taskify.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskify.model.Prioridad
import com.example.taskify.model.Tareas
import com.example.taskify.ui.theme.AzulBoton
import com.example.taskify.ui.theme.AzulOscuro
import com.example.taskify.ui.theme.FondoModal
import com.example.taskify.ui.theme.TextoPrincipal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Diálogo modal para crear una nueva tarea. Maneja su propio estado interno
 * (título, fecha, hora, prioridad, recordatorio) y notifica al llamador
 * solo cuando el usuario presiona Guardar, entregando el objeto Tareas listo.
 * Contiene dos sub-diálogos: DatePickerDialog y AlertDialog con TimePicker.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaTareaDialog(
    onDismiss: () -> Unit,
    onGuardar: (Tareas) -> Unit
) {
    var titulo by remember { mutableStateOf("") }                          // Texto del título
    var descripcion by remember { mutableStateOf("") }                    // Texto de descripción
    var fecha by remember { mutableStateOf("Seleccionar fecha") }         // Fecha seleccionada en "dd/MM/yyyy"
    var hora by remember { mutableStateOf("Seleccionar hora") }           // Hora seleccionada en "HH:mm"
    var prioridadSeleccionada by remember { mutableStateOf(Prioridad.MEDIA) } // Prioridad activa
    var activarRecordatorio by remember { mutableStateOf(false) }         // Estado del switch

    var mostrarDatePicker by remember { mutableStateOf(false) }           // Controla visibilidad del DatePicker
    var mostrarTimePicker by remember { mutableStateOf(false) }           // Controla visibilidad del TimePicker

    // Instancia de Calendar para inicializar el TimePicker en la hora actual
    val calendar = Calendar.getInstance()

    AlertDialog(
        containerColor = Color(0xFFFFF9F9),
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Nueva Tarea", color = Color(0xFF565050), fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                // Campo título
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text(text = "Título") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulOscuro, unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = AzulOscuro, unfocusedLabelColor = Color.Gray, cursorColor = AzulOscuro
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Campo descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text(text = "Descripción", color = Color(0xFF9A9898)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulOscuro, unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = AzulOscuro, unfocusedLabelColor = Color.Gray, cursorColor = AzulOscuro
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón que abre el DatePicker
                OutlinedButton(onClick = { mostrarDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = fecha, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Botón que abre el TimePicker
                OutlinedButton(onClick = { mostrarTimePicker = true }, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = hora, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Selector de prioridad con tres botones que cambian de color al seleccionarse
                Text(text = "Prioridad", color = Color(0xFF9A9898), fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = { prioridadSeleccionada = Prioridad.BAJA },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (prioridadSeleccionada == Prioridad.BAJA) Color(0xFF4CAF50) else Color(0xFFEAEAEA)
                        )
                    ) { Text("Baja", color = Color.Black) }

                    Button(
                        onClick = { prioridadSeleccionada = Prioridad.MEDIA },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (prioridadSeleccionada == Prioridad.MEDIA) Color(0xFFFFC107) else Color(0xFFEAEAEA)
                        )
                    ) { Text("Media", color = Color.Black) }

                    Button(
                        onClick = { prioridadSeleccionada = Prioridad.ALTA },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (prioridadSeleccionada == Prioridad.ALTA) Color(0xFFF44336) else Color(0xFFEAEAEA)
                        )
                    ) { Text("Alta", color = Color.White) }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Switch de recordatorio
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Activar recordatorio", color = Color(0xFF9A9898))
                    Switch(
                        checked = activarRecordatorio,
                        onCheckedChange = { activarRecordatorio = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AzulOscuro, checkedTrackColor = AzulBoton, checkedBorderColor = AzulOscuro
                        )
                    )
                }
            }
        },
        confirmButton = {
            // Construye el objeto Tareas y lo entrega al ViewModel
            Button(
                onClick = {
                    onGuardar(Tareas(titulo = titulo, descripcion = descripcion, fecha = fecha, hora = hora, prioridad = prioridadSeleccionada, recordatorio = activarRecordatorio))
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA8D8EA))
            ) { Text("Guardar", color = Color.Black) }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }, shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
            ) { Text("Cancelar", color = Color.Black) }
        }
    )

    // Sub-diálogo: DatePicker para seleccionar la fecha
    if (mostrarDatePicker) {
        val estadoFecha = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        estadoFecha.selectedDateMillis?.let { millis ->
                            // Formatea los milisegundos seleccionados a "dd/MM/yyyy" en UTC
                            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                                timeZone = java.util.TimeZone.getTimeZone("UTC")
                            }
                            fecha = formato.format(Date(millis))
                            activarRecordatorio = true // Activa recordatorio automáticamente al elegir fecha
                        }
                        mostrarDatePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulOscuro, contentColor = Color.White)
                ) { Text("Aceptar") }
            },
            dismissButton = {
                Button(onClick = { mostrarDatePicker = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0), contentColor = Color.Black)
                ) { Text("Cancelar") }
            }
        ) {
            DatePicker(
                state = estadoFecha,
                colors = DatePickerDefaults.colors(
                    containerColor = FondoModal, titleContentColor = TextoPrincipal,
                    headlineContentColor = TextoPrincipal, weekdayContentColor = Color.Gray,
                    dayContentColor = TextoPrincipal, todayContentColor = AzulOscuro,
                    selectedDayContainerColor = AzulOscuro, selectedDayContentColor = Color.White,
                    todayDateBorderColor = AzulOscuro
                )
            )
        }
    }

    // Sub-diálogo: TimePicker para seleccionar la hora
    if (mostrarTimePicker) {
        // Inicializa el reloj con la hora actual del dispositivo
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
                        // Formatea hora y minutos con ceros a la izquierda: "09:05"
                        hora = String.format(Locale.getDefault(), "%02d:%02d", estadoHora.hour, estadoHora.minute)
                        activarRecordatorio = true
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
                        periodSelectorBorderColor = AzulOscuro,
                        periodSelectorSelectedContainerColor = AzulOscuro,
                        periodSelectorUnselectedContainerColor = Color.Transparent,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = TextoPrincipal,
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
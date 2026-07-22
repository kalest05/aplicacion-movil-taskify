package com.example.taskify.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavController
import com.example.taskify.components.BottomMenu
import com.example.taskify.components.PantallaSeleccionada
import com.example.taskify.model.Tareas
import com.example.taskify.ui.theme.AzulOscuro
import com.example.taskify.ui.theme.FondoModal
import com.example.taskify.ui.theme.TextoPrincipal
import com.example.taskify.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Colores pastel para las tareas en la línea de tiempo
val coloresTarea = listOf(
    Color(0xFFCEB4F0), // morado pastel
    Color(0xFF99D9EA), // azul pastel
    Color(0xFFB5EAD7), // verde pastel
    Color(0xFFFFC8C8), // rosa pastel
    Color(0xFFFFF1A8)  // amarillo pastel
)

private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

// Extrae la hora (0-23) desde el texto "HH:mm" guardado en la tarea
private fun horaDeLaTarea(tarea: Tareas): Int? {
    return tarea.hora.substringBefore(":").trim().toIntOrNull()
}

/**
 * Pantalla de calendario muestra una barra horizontal con 14 días desde hoy
 * y al seleccionar un día filtra las tareas de ese día desde el TaskViewModel.
 * Las tareas se muestran en dos secciones: línea de tiempo de 24h y lista resumen.
 * No tiene su propia base de datos, reutiliza los datos del mismo Database de tareas.
 */
@Composable
fun PantallaCalendario(
    navController: NavController,
    tareasViewModel: TaskViewModel = TaskViewModel()
) {
    LaunchedEffect(Unit) {
        tareasViewModel.refrescarTareas()
    }

    val hoy = remember { Calendar.getInstance() }

    // Genera 14 días desde hoy
    val dias = remember {
        (0..13).map { offset ->
            val cal = hoy.clone() as Calendar
            cal.add(Calendar.DAY_OF_MONTH, offset)
            cal
        }
    }

    var diaSeleccionado by remember { mutableStateOf(dias.first()) }

    val fechaSeleccionadaTexto = formatoFecha.format(diaSeleccionado.time)

    // Tareas del día seleccionado
    val tareasDia = tareasViewModel.listaTareas.filter {
        it.fecha == fechaSeleccionadaTexto
    }

    // Mapa hora (0-23) -> lista de tareas en esa hora
    val tareasPorHora: Map<Int, List<Tareas>> = tareasDia
        .mapNotNull { tarea -> horaDeLaTarea(tarea)?.let { it to tarea } }
        .groupBy({ it.first }, { it.second })

    // Lista de etiquetas para las 24 horas del día en formato 12h (AM/PM)
    val horas = (0..23).map { hora ->
        when {
            hora == 0 -> "12:00 AM"
            hora < 12 -> "$hora:00 AM"
            hora == 12 -> "12:00 PM"
            else -> "${hora - 12}:00 PM"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE4F2F7))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            // Título
            Text(
                text = "Calendario",
                color = TextoPrincipal,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp, bottom = 16.dp)
            )

            // Card con barra horizontal de días (LazyRow scrolleable)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FondoModal)
            ) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(dias) { dia ->
                        val seleccionado = formatoFecha.format(dia.time) ==
                                formatoFecha.format(diaSeleccionado.time)

                        val nombreDia = SimpleDateFormat("EEE", Locale("es"))
                            .format(dia.time)
                            .replaceFirstChar { it.uppercase() }
                            .removeSuffix(".")

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .clickable { diaSeleccionado = dia }
                        ) {
                            Text(
                                text = nombreDia,
                                fontSize = 11.sp,
                                color = if (seleccionado) AzulOscuro else Color.Gray,
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = if (seleccionado) AzulOscuro else Color.Transparent,
                                        shape = CircleShape
                                    )
                            ) {
                                Text(
                                    text = dia.get(Calendar.DAY_OF_MONTH).toString(),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (seleccionado) Color.White else TextoPrincipal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {

                // Encabezado fecha seleccionada
                item {
                    val nombreDiaCompleto = SimpleDateFormat("EEEE", Locale("es"))
                        .format(diaSeleccionado.time)
                        .replaceFirstChar { it.uppercase() }
                    val diaNumero = diaSeleccionado.get(Calendar.DAY_OF_MONTH)
                    val mes = SimpleDateFormat("MMMM", Locale("es"))
                        .format(diaSeleccionado.time)
                        .replaceFirstChar { it.uppercase() }

                    Text(
                        text = "$nombreDiaCompleto, $diaNumero de $mes",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoPrincipal,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Línea de tiempo con horas
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FondoModal)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            horas.forEachIndexed { index, hora ->
                                val tareasEnEstaHora = tareasPorHora[index].orEmpty()

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 44.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = hora,
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.width(64.dp)
                                    )

                                    if (tareasEnEstaHora.isNotEmpty()) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            tareasEnEstaHora.forEachIndexed { tareaIndex, tarea ->
                                                val colorTarea = coloresTarea[
                                                    (index + tareaIndex) % coloresTarea.size
                                                ]
                                                Box(
                                                    contentAlignment = Alignment.CenterStart,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(32.dp)
                                                        .background(
                                                            color = colorTarea,
                                                            shape = RoundedCornerShape(8.dp)
                                                        )
                                                        .padding(horizontal = 12.dp)
                                                ) {
                                                    Text(
                                                        text = "${tarea.titulo} (${tarea.hora})",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = TextoPrincipal
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        HorizontalDivider(
                                            modifier = Modifier.fillMaxWidth(),
                                            color = Color(0xFFEEEEEE),
                                            thickness = 1.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Card "Tareas del día"
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FondoModal)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Tareas del día",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            if (tareasDia.isEmpty()) {
                                Text(
                                    text = "Sin tareas para este día",
                                    fontSize = 13.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                val tareasOrdenadas = tareasDia.sortedBy { tarea ->
                                    horaDeLaTarea(tarea) ?: 24
                                }
                                tareasOrdenadas.forEachIndexed { index, tarea ->
                                    FilaTareaCalendario(
                                        tarea = tarea,
                                        color = coloresTarea[index % coloresTarea.size]
                                    )
                                    if (index < tareasOrdenadas.lastIndex) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Menú inferior
        BottomMenu(
            pantallaActual = PantallaSeleccionada.CALENDARIO,
            onPantallaSeleccionada = { pantalla ->
                when (pantalla) {
                    PantallaSeleccionada.TAREAS -> navController.navigate("tareas")
                    PantallaSeleccionada.ALARMAS -> navController.navigate("alarmas")
                    PantallaSeleccionada.CALENDARIO -> {}
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Fila de resumen para una tarea en la card "Tareas del día".
 * Muestra un círculo de color, el título y la hora de la tarea.
 * El color se recibe del mismo índice que se usa en la línea de tiempo.
 */
@Composable
fun FilaTareaCalendario(tarea: Tareas, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = tarea.titulo,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoPrincipal
            )
            Text(
                text = tarea.hora,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
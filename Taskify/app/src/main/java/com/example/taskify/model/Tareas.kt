package com.example.taskify.model

/**
 * Modelo de datos para una tarea. Usa data class para aprovechar
 * copy() al momento de actualizar campos sin modificar el objeto original.
 */
data class Tareas(
    val id: Int = 0,                        // Identificador único, asignado por el ViewModel
    val titulo: String,                     // Nombre corto de la tarea
    val descripcion: String,                // Detalles opcionales de la tarea
    val fecha: String,                      // Fecha en formato "dd/MM/yyyy", ej: "30/06/2026"
    val hora: String,                       // Hora en formato "HH:mm", ej: "14:30"
    val prioridad: Prioridad,               // Nivel de urgencia (ALTA, MEDIA, BAJA)
    val completada: Boolean = false,        // Si el checkbox de la tarea fue marcado
    val recordatorio: Boolean = false       // Si tiene una alarma asociada activa
)
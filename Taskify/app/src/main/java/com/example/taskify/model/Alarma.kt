package com.example.taskify.model

/**
 * Modelo de datos para una alarma. Usa data class para tener
 * equals, hashCode y copy() automáticos, útiles al actualizar en el repositorio.
 */
data class Alarma(
    val id: Int = 0,                            // Identificador único de la alarma
    val hora: String,                           // Hora en formato "HH:mm", ej: "07:00"
    val descripcion: String,                    // Texto descriptivo de la alarma
    val dias: List<DiaSemana> = DiaSemana.entries, // Días de la semana en que suena
    val activa: Boolean = true,                 // Si la alarma está encendida o apagada
    val tareaId: Int? = null                    // Id de la tarea vinculada, null si es manual
)

/**
 * Enumeración de los días de la semana con su etiqueta corta para la UI.
 * Se usa para recorrer y mostrar los círculos de días en orden.
 */
enum class DiaSemana(val etiqueta: String) {
    LUNES("L"),
    MARTES("M"),
    MIERCOLES("Mi"),
    JUEVES("J"),
    VIERNES("V"),
    SABADO("S"),
    DOMINGO("D")
}
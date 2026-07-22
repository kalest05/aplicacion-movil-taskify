package com.example.taskify.data

import com.example.taskify.model.Alarma
import com.example.taskify.model.DiaSemana
import com.example.taskify.model.Tareas
import kotlin.collections.MutableList


/**
 * Base de datos en memoria de la app. Al ser un objecto,
 * solo existe una instancia compartida por todos los repositorios.
 * Los datos se pierden al cerrar la app ya que no hay persistencia externa.
 */
object Database {
    // Lista mutable que almacena todas las tareas creadas por el usuario
    val tareas = mutableListOf<Tareas>()

    // Lista mutable que almacena todas las alarmas, incluyendo las generadas automáticamente
    val alarmas = mutableListOf<Alarma>()
}
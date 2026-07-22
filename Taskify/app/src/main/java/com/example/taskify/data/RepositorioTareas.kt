package com.example.taskify.data

import com.example.taskify.model.Tareas

/**
 * Capa intermedia entre Database y TaskViewModel.
 * Centraliza todas las operaciones sobre la lista de tareas,
 * de modo que si se cambia la fuente de datos, solo se modifica aquí.
 */
class RepositorioTareas {

    /**
     * Devuelve la lista completa de tareas directamente desde Database.
     * Retorna la referencia mutable, por lo que cambios en Database se reflejan.
     */
    fun obtenerTareas(): MutableList<Tareas> {
        return Database.tareas
    }

    /**
     * Agrega una nueva tarea al final de la lista en Database.
     * tarea es el objeto Tareas ya construido con su id asignado.
     */
    fun agregarTarea(tarea: Tareas) {
        Database.tareas.add(tarea)
    }

    /**
     * Elimina una tarea de la lista comparando por referencia de objeto.
     * tarea es la tarea exacta que se desea eliminar.
     */
    fun eliminarTarea(tarea: Tareas) {
        Database.tareas.remove(tarea)
    }

    /**
     * Busca una tarea por su id y la reemplaza con la versión actualizada.
     * Si no encuentra el id, no hace ningún cambio.
     * tareaActualizada es la tarea con el mismo id pero campos modificados.
     */
    fun actualizarTarea(tareaActualizada: Tareas) {
        val index = Database.tareas.indexOfFirst { it.id == tareaActualizada.id }
        if (index != -1) {
            Database.tareas[index] = tareaActualizada
        }
    }
}